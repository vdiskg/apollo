package com.ctrip.framework.apollo.portal.spi.oidc;

import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import com.ctrip.framework.apollo.portal.spi.UserService;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public interface OidcLocalUserService extends UserService {

  /**
   * create local user info related to the oidc user
   *
   * @param newUserInfo the oidc user's info
   */
  void createLocalUser(UserInfo newUserInfo);

  /**
   * update user's info
   *
   * @param newUserInfo the new user's info
   */
  void updateUserInfo(UserInfo newUserInfo);
}
