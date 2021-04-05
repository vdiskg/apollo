package com.ctrip.framework.apollo.portal.controller;

import com.ctrip.framework.apollo.common.dto.CommitDTO;
import com.ctrip.framework.apollo.common.utils.PreferredUsernameUtil;
import com.ctrip.framework.apollo.portal.component.PermissionValidator;
import com.ctrip.framework.apollo.portal.environment.Env;
import com.ctrip.framework.apollo.portal.service.CommitService;
import com.ctrip.framework.apollo.portal.spi.UserService;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Validated
@RestController
public class CommitController {

  private final CommitService commitService;
  private final PermissionValidator permissionValidator;
  private final UserService userService;

  public CommitController(final CommitService commitService,
      final PermissionValidator permissionValidator,
      final UserService userService) {
    this.commitService = commitService;
    this.permissionValidator = permissionValidator;
    this.userService = userService;
  }

  @GetMapping("/apps/{appId}/envs/{env}/clusters/{clusterName}/namespaces/{namespaceName}/commits")
  public List<CommitDTO> find(@PathVariable String appId, @PathVariable String env,
                              @PathVariable String clusterName, @PathVariable String namespaceName,
                              @Valid @PositiveOrZero(message = "page should be positive or 0") @RequestParam(defaultValue = "0") int page,
                              @Valid @Positive(message = "size should be positive number") @RequestParam(defaultValue = "10") int size) {
    if (permissionValidator.shouldHideConfigToCurrentUser(appId, env, namespaceName)) {
      return Collections.emptyList();
    }
    List<CommitDTO> dtoList = commitService
        .find(appId, Env.valueOf(env), clusterName, namespaceName, page, size);
    if (CollectionUtils.isEmpty(dtoList)) {
      return Collections.emptyList();
    }
    Set<String> operatorIdSet = PreferredUsernameUtil.extractOperatorId(dtoList);
    Map<String, String> preferredUsernameMap = userService
        .findPreferredUsernameMapByUserIds(new ArrayList<>(operatorIdSet));
    if (CollectionUtils.isEmpty(preferredUsernameMap)) {
      return dtoList;
    }
    dtoList.forEach(dto -> PreferredUsernameUtil.setPreferredUsername(dto, preferredUsernameMap));
    return dtoList;
  }
}
