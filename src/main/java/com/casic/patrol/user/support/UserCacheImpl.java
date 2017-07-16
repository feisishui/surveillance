package com.casic.patrol.user.support;


import com.casic.patrol.api.user.UserCache;
import com.casic.patrol.api.user.UserDTO;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.cache.Cache;
import javax.cache.CacheManager;

public class UserCacheImpl implements UserCache {
    private CacheManager cacheManager;
    private Cache<String, UserDTO> cache;

    @PostConstruct
    public void init() {
        this.cache = this.cacheManager.getCache("user");
    }

    public UserDTO findById(String id) {
        String key = "userId:" + id;

        return cache.get(key);
    }

    public UserDTO findByUsername(String username, String userRepoRef) {
        String key = "userUsername:" + username + ":" + userRepoRef;

        return cache.get(key);
    }

    public UserDTO findByRef(String ref, String userRepoRef) {
        String key = "userRef:" + ref + ":" + userRepoRef;

        return cache.get(key);
    }

    public UserDTO findByNickName(String nickName) {
        String key = "nickName:" + nickName;

        return cache.get(key);
    }

    public void updateUser(UserDTO userDto) {
        this.removeUser(userDto);

        // cache.set("userId:" + userDto.getId(), userDto);
        // cache.set(
        // "userUsername:" + userDto.getUsername() + ":"
        // + userDto.getUserRepoRef(), userDto);
        // cache.set(
        // "userRef:" + userDto.getRef() + ":" + userDto.getUserRepoRef(),
        // userDto);
        // cache.set("nickName:" + userDto.getDisplayName(), userDto);
    }

    public void removeUser(UserDTO userDto) {
        cache.remove("userId:" + userDto.getId());
        cache.remove("userUsername:" + userDto.getUsername() + ":"
                + userDto.getUserRepoRef());
        cache.remove("userRef:" + userDto.getRef() + ":"
                + userDto.getUserRepoRef());
        cache.remove("nickName:" + userDto.getDisplayName());
    }

    @Resource
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
