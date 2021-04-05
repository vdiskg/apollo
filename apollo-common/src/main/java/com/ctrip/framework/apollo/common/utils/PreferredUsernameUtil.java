package com.ctrip.framework.apollo.common.utils;

import com.ctrip.framework.apollo.common.dto.BaseDTO;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class PreferredUsernameUtil {

  /**
   * extract operator id from the dto list
   *
   * @param dtoList dto list with operator id
   * @return operator id set
   */
  public static Set<String> extractOperatorId(List<? extends BaseDTO> dtoList) {
    if (CollectionUtils.isEmpty(dtoList)) {
      return Collections.emptySet();
    }
    Set<String> operatorIdSet = new HashSet<>();
    for (BaseDTO dto : dtoList) {
      operatorIdSet.add(dto.getDataChangeCreatedBy());
      operatorIdSet.add(dto.getDataChangeLastModifiedBy());
    }
    return operatorIdSet;
  }

  /**
   * extract operator id from the dto
   *
   * @param dto dto with operator id
   * @return operator id set
   */
  public static Set<String> extractOperatorId(BaseDTO dto) {
    Set<String> operatorIdSet = new HashSet<>();
    operatorIdSet.add(dto.getDataChangeCreatedBy());
    operatorIdSet.add(dto.getDataChangeLastModifiedBy());
    return operatorIdSet;
  }

  /**
   * set the preferred username
   *
   * @param dto                  dto with operator id
   * @param preferredUsernameMap (userId - preferredUsername) prepared preferred username map
   */
  public static void setPreferredUsername(BaseDTO dto,
      Map<String, String> preferredUsernameMap) {
    if (CollectionUtils.isEmpty(preferredUsernameMap)) {
      return;
    }
    if (StringUtils.hasText(dto.getDataChangeCreatedBy())) {
      String preferredUsername = preferredUsernameMap.get(dto.getDataChangeCreatedBy());
      if (StringUtils.hasText(preferredUsername)) {
        dto.setDataChangeCreatedByPreferredUsername(preferredUsername);
      }
    }
    if (StringUtils.hasText(dto.getDataChangeLastModifiedBy())) {
      String preferredUsername = preferredUsernameMap.get(dto.getDataChangeLastModifiedBy());
      if (StringUtils.hasText(preferredUsername)) {
        dto.setDataChangeLastModifiedByPreferredUsername(preferredUsername);
      }
    }
  }
}
