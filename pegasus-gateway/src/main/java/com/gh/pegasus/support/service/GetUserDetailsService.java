package com.gh.pegasus.support.service;

import com.gh.pegasus.support.dto.CuxUserDetails;

import java.util.Map;

/**
 * Created by enHui.Chen on 2020/10/26.
 */
public interface GetUserDetailsService {
    CuxUserDetails buildUserDetails(Map<String, Object> userMap);
}
