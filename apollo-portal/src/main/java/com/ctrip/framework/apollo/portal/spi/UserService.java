package com.ctrip.framework.apollo.portal.spi;

import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public interface UserService {
  List<UserInfo> searchUsers(String keyword, int offset, int limit);

  UserInfo findByUserId(String userId);

  List<UserInfo> findByUserIds(List<String> userIds);

  /**
   * find the preferred username map
   *
   * @param userIds user id list
   * @return preferredUsernameMap (userId - preferredUsername)
   */
  default Map<String, String> findPreferredUsernameMapByUserIds(List<String> userIds) {
    if (CollectionUtils.isEmpty(userIds)) {
      return Collections.emptyMap();
    }
    List<UserInfo> userInfoList = this.findByUserIds(userIds);
    if (CollectionUtils.isEmpty(userInfoList)) {
      return Collections.emptyMap();
    }
    return userInfoList.stream()
        .collect(Collectors.toMap(UserInfo::getUserId, UserInfo::getName));
  }
}
