package com.gh.pegasus.support.service.impl;

import com.gh.pegasus.support.dto.CuxUserDetails;
import com.gh.pegasus.support.service.GetUserDetailsService;
import com.pegasus.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by enHui.Chen on 2020/10/26.
 */
@Slf4j
@Service
public class GetUserDetailsServiceImpl implements GetUserDetailsService {
    @Override
    public CuxUserDetails buildUserDetails(Map<String, Object> userMap) {
        Map<String, Object> userAuthentication = (LinkedHashMap<String, Object>) userMap.get("principal");
        CuxUserDetails cuxUserDetails = new CuxUserDetails();
        try {
            BeanUtils.populate(cuxUserDetails, userAuthentication);
            Object roleIds = userAuthentication.get("roleIds");
            if (roleIds instanceof List) {
                List<?> roleIdsL = (List<?>) roleIds;
                cuxUserDetails.setRoleIds(roleIdsL.stream().map(roleId -> Long.valueOf(String.valueOf(roleId))).collect(Collectors.toList()));
            }

            Object clientOnly = userMap.get("clientOnly");
            cuxUserDetails.setClientOnly(clientOnly == null ? null : (Boolean) clientOnly);

            Object oauth2RequestObj = userMap.get("oauth2Request");
            if (oauth2RequestObj instanceof Map) {
                Map<?, ?> oauth2Request = (LinkedHashMap<?, ?>) oauth2RequestObj;

                Object clientId = oauth2Request.get("clientId");
                Object grantType = oauth2Request.get("grantType");
                Object refresh = oauth2Request.get("refresh");
                Object approved = oauth2Request.get("approved");
                Object redirectUri = oauth2Request.get("redirectUri");
                Object scope = oauth2Request.get("scope");
                Object resourceId = oauth2Request.get("resourceIds");

                cuxUserDetails.setClientId(clientId == null ? null : (String) clientId);
                cuxUserDetails.setGrantType(grantType == null ? null : (String) grantType);
                cuxUserDetails.setRefresh(refresh == null ? null : (Boolean) refresh);
                cuxUserDetails.setApproved(approved == null ? null : (Boolean) approved);
                cuxUserDetails.setRedirectUri(redirectUri == null ? null : (String) redirectUri);

                if (scope instanceof Collection) {
                    Set<?> scopes = new HashSet<>((List<?>) scope);

                    cuxUserDetails.setScope(scopes.stream().map(String::valueOf).collect(Collectors.toSet()));
                }

                if (resourceId instanceof List) {
                    Set<?> resourceIds = new HashSet<>((List<?>) resourceId);
                    cuxUserDetails.setScope(resourceIds.stream().map(String::valueOf).collect(Collectors.toSet()));
                }
            }


        } catch (Exception e) {
            log.error("populate userAuthentication error:{}", userMap.toString());
            throw new CommonException(e);
        }
        return cuxUserDetails;
    }
}
