package ru.otus.hw.services;

import org.springframework.security.acls.model.Permission;
import ru.otus.hw.security.IAclConfigurable;
import ru.otus.hw.security.PermissionGroup;

import java.util.List;
import java.util.Map;

public interface AclServiceWrapperService {

    <T extends IAclConfigurable> void createPermission(T object, Map<PermissionGroup, List<Permission>> permissionMap);
}
