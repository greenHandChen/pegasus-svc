package com.pegasus.security.sso.cas.constants;

import com.pegasus.security.constant.Constant;

/**
 * Created by enHui.Chen on 2021/3/23.
 */
public interface CasConstant {
    // cas的ticket
    String SSO_CAS_TICKET = "ticket";

    // cas的ticket-token
    String SSO_CAS_TICKET_TOKEN = Constant.PE_OAUTH + ":sso-cas:ticket_token:";

    // cas的token-ticket
    String SSO_CAS_TOKEN_TICKET = Constant.PE_OAUTH + ":sso-cas:token_ticket:";

}