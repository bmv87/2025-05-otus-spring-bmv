package ru.otus.hw.security;

import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class AclMethodSecurityExpressionRoot implements SecurityExpressionOperations,
        AclMethodSecurityExpressionOperations {

    private MethodSecurityExpressionOperations root;

    private final String read;

    private final String write;

    private final String delete;

    private final String admin;

    public AclMethodSecurityExpressionRoot(MethodSecurityExpressionOperations operations) {
        root = operations;
        this.read = "read";
        this.write = "write";
        this.delete = "delete";
        this.admin = "administration";
    }

    boolean isGranted(Object targetId, Class<?> targetClass, Object permission) {
        return hasPermission(targetId, targetClass.getCanonicalName(), permission);
    }


    public boolean isAdministrator(Object targetId, Class<?> targetClass) {

        return isGranted(targetId, targetClass, admin);
    }

    @Override
    public boolean isAdministrator(Object target) {

        return root.hasPermission(target, admin);
    }

    @Override
    public boolean canRead(Object targetId, Class<?> targetClass) {

        if (isAdministrator(targetId, targetClass)) {
            return true;
        }

        return isGranted(targetId, targetClass, read);
    }

    @Override
    public boolean canDelete(Object targetId, Class<?> targetClass) {

        if (isAdministrator(targetId, targetClass)) {
            return true;
        }

        return isGranted(targetId, targetClass, delete);
    }

    @Override
    public boolean canWrite(Object targetId, Class<?> targetClass) {

        if (isAdministrator(targetId, targetClass)) {
            return true;
        }

        return isGranted(targetId, targetClass, write);
    }

    @Override
    public Authentication getAuthentication() {
        return root.getAuthentication();
    }

    @Override
    public boolean hasAuthority(String authority) {
        return root.hasAuthority(authority);
    }

    @Override
    public boolean hasAnyAuthority(String... authorities) {
        return root.hasAnyAuthority(authorities);
    }

    @Override
    public boolean hasRole(String role) {
        return root.hasRole(role);
    }

    @Override
    public boolean hasAnyRole(String... roles) {
        return root.hasAnyRole(roles);
    }

    @Override
    public boolean permitAll() {
        return root.permitAll();
    }

    @Override
    public boolean denyAll() {
        return root.denyAll();
    }

    @Override
    public boolean isAnonymous() {
        return root.isAnonymous();
    }

    @Override
    public boolean isAuthenticated() {
        return root.isAuthenticated();
    }

    @Override
    public boolean isRememberMe() {
        return root.isRememberMe();
    }

    @Override
    public boolean isFullyAuthenticated() {
        return root.isFullyAuthenticated();
    }

    @Override
    public boolean hasPermission(Object target, Object permission) {
        return root.hasPermission(target, permission);
    }

    @Override
    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return root.hasPermission(targetId, targetType, permission);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        root.setReturnObject(filterObject);
    }

    @Override
    public Object getFilterObject() {
        return root.getFilterObject();
    }

    @Override
    public void setReturnObject(Object returnObject) {
        root.setReturnObject(returnObject);
    }

    @Override
    public Object getReturnObject() {
        return root.getReturnObject();
    }

    @Override
    public Object getThis() {
        return root.getThis();
    }
}
