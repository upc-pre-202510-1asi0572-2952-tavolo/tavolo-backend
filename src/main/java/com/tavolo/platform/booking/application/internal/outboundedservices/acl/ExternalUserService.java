package com.tavolo.platform.booking.application.internal.outboundedservices.acl;

import com.tavolo.platform.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

@Service
public class ExternalUserService {
    private final IamContextFacade iamContextFacade;

    public ExternalUserService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public boolean existUserById(Long userId) {
        return iamContextFacade.existsUser(userId);
    }
}
