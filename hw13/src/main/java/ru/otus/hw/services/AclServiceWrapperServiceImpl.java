package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.hw.security.IAclConfigurable;
import ru.otus.hw.security.PermissionGroup;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import static ru.otus.hw.security.AuthorityConstants.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private final MutableAclService mutableAclService;

    @Override
    public <T extends IAclConfigurable> void createPermission(
            T object, Map<PermissionGroup,
            List<Permission>> permissionMap) {
        if (permissionMap.entrySet().isEmpty()) {
            throw new InvalidParameterException("permissionMap is empty");
        }
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        MutableAcl acl = mutableAclService.createAcl(oid);

        if (permissionMap.containsKey(PermissionGroup.CURRENT)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final Sid owner = new PrincipalSid(authentication);
            for (var permission : permissionMap.get(PermissionGroup.CURRENT)) {
                acl.insertAce(acl.getEntries().size(), permission, owner, true);
            }
        }

        if (permissionMap.containsKey(PermissionGroup.ADMIN)) {
            final Sid admin = new GrantedAuthoritySid(ROLE_ADMIN);
            for (var permission : permissionMap.get(PermissionGroup.ADMIN)) {
                acl.insertAce(acl.getEntries().size(), permission, admin, true);
            }
        }

        mutableAclService.updateAcl(acl);
    }
}
