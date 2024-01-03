package com.ebanking.IdentityProvider.user;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    BACK_OFFICE_READ("BACK_OFFICE_READ:read"),
    BACK_OFFICE_UPDATE("BACK_OFFICE_READ:update"),
    BACK_OFFICE_CREATE("BACK_OFFICE_READ:create"),
    BACK_OFFICE_DELETE("BACK_OFFICE_READ:delete"),
    AGENT_READ("AGENT:read"),
    AGENT_UPDATE("AGENT:update"),
    AGENT_CREATE("AGENT:create"),
    AGENT_DELETE("AGENT:delete")

    ;

    @Getter
    private final String permission;
}
