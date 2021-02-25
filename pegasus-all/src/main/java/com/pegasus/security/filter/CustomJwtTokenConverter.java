package com.pegasus.security.filter;

import com.pegasus.common.exception.CommonException;
import com.pegasus.common.utils.JsonUtil;
import com.pegasus.security.dto.CuxUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by enHui.Chen on 2020/10/27.
 */
@Slf4j
public class CustomJwtTokenConverter extends DefaultAccessTokenConverter {

    /**
     * @Author: enHui.Chen
     * @Description: 根据用户信息生成认证对象
     * @Data 2020/10/27
     */
    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication authentication = super.extractAuthentication(map);
        CuxUserDetails cuxUserDetails = new CuxUserDetails((String) map.get("username"), "******", Collections.EMPTY_LIST);
        try {
            BeanUtils.populate(cuxUserDetails, map);
            List<Object> roleIds = (List) map.get("roleIds");
            if (CollectionUtils.isNotEmpty(roleIds)) {
                cuxUserDetails.setRoleIds(roleIds.stream().map(roleId -> Long.valueOf(String.valueOf(roleId))).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            log.error("trans cuxUserDetails error:{}", JsonUtil.toString(map));
            throw new CommonException(e);
        }
        authentication.setDetails(cuxUserDetails);
        return authentication;
    }
}
