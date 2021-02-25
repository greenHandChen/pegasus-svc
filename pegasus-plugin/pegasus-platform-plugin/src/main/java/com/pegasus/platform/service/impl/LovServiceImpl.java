package com.pegasus.platform.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pegasus.common.service.impl.CommonServiceImpl;
import com.pegasus.common.utils.JsonUtil;
import com.pegasus.platform.domain.Lov;
import com.pegasus.platform.domain.LovItem;
import com.pegasus.platform.service.ILovItemService;
import com.pegasus.platform.service.ILovService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by enHui.Chen on 2019/11/11.
 */
@Slf4j
@Service
public class LovServiceImpl extends CommonServiceImpl<Lov> implements ILovService<Lov> {
    @Autowired
    private ILovItemService lovItemService;
    @Autowired
    @Qualifier("oAuth2RestTemplate")
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Override
    public Map<String, Object> lovQuery(Pageable pageable, String lovCode, Boolean init) {
        Lov lov = new Lov();
        lov.setLovCode(lovCode);
        List<Lov> lovs = super.select(lov);
        if (CollectionUtils.isEmpty(lovs)) {
            return null;
        }

        Map<String, Object> rst = new HashMap<>();
        lov = lovs.get(0);

        // 渲染lov的数据
        List<LovItem> lovItems;
        if (init) {
            lovItems = lovItemService.select(LovItem.builder().lovId(lov.getId()).build());
            if (!CollectionUtils.isEmpty(lovItems)) {
                List<LovItem> columns = lovItems.stream().filter(LovItem::getIsColumnField).collect(Collectors.toList());
                List<LovItem> conditions = lovItems.stream().filter(LovItem::getIsConditionField).collect(Collectors.toList());
                rst.put("columns", columns);
                rst.put("conditions", conditions);
            }
        }

        // 渲染lovTable的数据
        if (!StringUtils.isEmpty(lov.getCustomSql())) {

        }

        // 必须为get请求,请求参数限制在url上
        Map<String, Object> urlMap = null;
        if (!StringUtils.isEmpty(lov.getCustomUrl())) {
            String response = oAuth2RestTemplate.getForObject(generatePageParam(pageable, lov.getCustomUrl()), String.class);
            urlMap = JsonUtil.toObject(response, new TypeReference<Map<String, Object>>() {
            });
            rst.putAll(urlMap);
        }

        return rst;
    }

    private String generatePageParam(Pageable pageable, String lovUrl) {
        StringBuilder url = new StringBuilder(lovUrl);
        return url.append(url.indexOf("?") == -1 ? "?page=" : "&page=")
                .append(pageable.getPageNumber())
                .append("&size=")
                .append(pageable.getPageSize())
                .toString();
    }
}
