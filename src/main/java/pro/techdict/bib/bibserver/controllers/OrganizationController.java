package pro.techdict.bib.bibserver.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.techdict.bib.bibserver.entities.Organization;
import pro.techdict.bib.bibserver.models.OrganizationScope;
import pro.techdict.bib.bibserver.services.OrganizationService;
import pro.techdict.bib.bibserver.utils.HttpResponse;

import java.util.Map;

@RestController
@RequestMapping("/org")
public class OrganizationController {

  final OrganizationService organizationService;

  public OrganizationController(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  @PostMapping("/")
  public HttpResponse createNewOrganization(@RequestBody Map<String, String> requestBody) {
    String name = requestBody.get("name");
    String desc = requestBody.get("desc");
    String avatarURL = requestBody.get("avatarURL");
    Long creatorUid = Long.parseLong(requestBody.get("creatorUid"));
    int scopeVal = Integer.parseInt(requestBody.get("scope"));
    OrganizationScope scope = OrganizationScope.PRIVATE;
    if (scopeVal > 0) {
      scope = OrganizationScope.PUBLIC;
    }

    Organization newOrganization =
        organizationService.createNewOrganization(name, desc, scope, avatarURL, creatorUid);
    return HttpResponse.success("创建团队成功！", newOrganization);
  }

}
