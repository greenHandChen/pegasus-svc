package com.gh.pegasus.service;

import com.gh.pegasus.domain.User;
import com.gh.pegasus.security.UsernamePasswordSubSystemCredential;

/**
 * Created by enHui.Chen on 2021/2/26.
 */
public interface IUserDetailsService {
    /**
     * @param upssc
     * @throws
     * @desc 用户认证
     * @date 2021-02-26 23:56:22
     * @return com.gh.pegasus.domain.User
     */
    User authenticate(UsernamePasswordSubSystemCredential upssc);
}
