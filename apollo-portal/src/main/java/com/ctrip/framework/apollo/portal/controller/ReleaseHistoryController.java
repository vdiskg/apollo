package com.ctrip.framework.apollo.portal.controller;


import com.ctrip.framework.apollo.portal.component.PermissionValidator;
import com.ctrip.framework.apollo.portal.entity.bo.ReleaseHistoryBO;
import com.ctrip.framework.apollo.portal.environment.Env;
import com.ctrip.framework.apollo.portal.service.ReleaseHistoryService;
import com.ctrip.framework.apollo.portal.spi.UserService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ReleaseHistoryController {

  private final ReleaseHistoryService releaseHistoryService;
  private final PermissionValidator permissionValidator;
  private final UserService userService;

  public ReleaseHistoryController(final ReleaseHistoryService releaseHistoryService,
      final PermissionValidator permissionValidator,
      final UserService userService) {
    this.releaseHistoryService = releaseHistoryService;
    this.permissionValidator = permissionValidator;
    this.userService = userService;
  }

  @GetMapping("/apps/{appId}/envs/{env}/clusters/{clusterName}/namespaces/{namespaceName}/releases/histories")
  public List<ReleaseHistoryBO> findReleaseHistoriesByNamespace(@PathVariable String appId,
                                                                @PathVariable String env,
                                                                @PathVariable String clusterName,
                                                                @PathVariable String namespaceName,
                                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size) {

    if (permissionValidator.shouldHideConfigToCurrentUser(appId, env, namespaceName)) {
      return Collections.emptyList();
    }
    List<ReleaseHistoryBO> releaseHistoryList = releaseHistoryService
        .findNamespaceReleaseHistory(appId, Env.valueOf(env), clusterName, namespaceName, page,
            size);
    if (CollectionUtils.isEmpty(releaseHistoryList)) {
      return Collections.emptyList();
    }
    Set<String> operatorIdSet = releaseHistoryList.stream()
        .map(ReleaseHistoryBO::getOperator)
        .collect(Collectors.toSet());
    Map<String, String> preferredUsernameMap = this.userService
        .findPreferredUsernameMapByUserIds(new ArrayList<>(operatorIdSet));
    if (CollectionUtils.isEmpty(preferredUsernameMap)) {
      return releaseHistoryList;
    }
    releaseHistoryList.forEach(releaseHistory -> {
      if (StringUtils.hasText(releaseHistory.getOperator())) {
        String preferredUsername = preferredUsernameMap.get(releaseHistory.getOperator());
        if (StringUtils.hasText(preferredUsername)) {
          releaseHistory.setOperatorPreferredUsername(preferredUsername);
        }
      }
    });
    return releaseHistoryList;
  }

}
