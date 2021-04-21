package pro.techdict.bib.bibserver.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.techdict.bib.bibserver.dtos.OrgSimpleDto;
import pro.techdict.bib.bibserver.entities.Organization;
import pro.techdict.bib.bibserver.models.CreateOrgFormModel;
import pro.techdict.bib.bibserver.services.OrganizationService;
import pro.techdict.bib.bibserver.services.TencentCOSService;
import pro.techdict.bib.bibserver.utils.COSUploadResultWithKey;
import pro.techdict.bib.bibserver.utils.COSUtils;
import pro.techdict.bib.bibserver.utils.HttpResponse;

import java.util.List;

@RestController
@RequestMapping("/org")
public class OrganizationController {

  final OrganizationService organizationService;
  final TencentCOSService cosService;

  public OrganizationController(
      OrganizationService organizationService,
      TencentCOSService cosService
  ) {
    this.organizationService = organizationService;
    this.cosService = cosService;
  }

  @PostMapping("/")
  public HttpResponse createNewOrganization(@RequestBody CreateOrgFormModel requestBody) {
    Organization newOrganization =
        organizationService.createNewOrganization(
            requestBody.getName(),
            requestBody.getDesc(),
            requestBody.getAvatarURL(),
            requestBody.getCreatorUid()
        );
    return HttpResponse.success("创建团队成功！", OrgSimpleDto.fromEntity(newOrganization));
  }

  @PostMapping("/uploadAvatar")
  public HttpResponse uploadOrganizationAvatar(
      @RequestParam("userId") Long creatorId,
      @RequestParam("avatarFile") MultipartFile[] uploadFiles
  ) {
    List<COSUploadResultWithKey> uploadResults =
        cosService.uploadObjects(COSUtils.getPrefixWithUserId(
            "bibweb/org-avatars/initializing/", creatorId),
            uploadFiles
        );
    return HttpResponse.success("上传团队头像成功！", uploadResults);
  }

}
