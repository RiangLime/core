package cn.lime.core.controller;

import cn.lime.core.annotation.ApiLimit;
import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.annotation.RequestLog;
import cn.lime.core.common.BaseResponse;
import cn.lime.core.common.BusinessException;
import cn.lime.core.common.PageResult;
import cn.lime.core.common.ResultUtils;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.module.dto.EmptyDto;
import cn.lime.core.module.dto.media.*;
import cn.lime.core.module.vo.LocalMediaTagVo;
import cn.lime.core.module.vo.LocalMediaVo;
import cn.lime.core.service.db.LocalMediaService;
import cn.lime.core.service.db.LocalMediaTagService;
import cn.lime.core.service.db.UserService;
import cn.lime.core.service.filestore.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName: FileStorageController
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/9/2 11:07
 */
@RestController
@RequestMapping("/filestorage")
@Tag(name = "多媒体上传接口")
@CrossOrigin(origins = "*")
@RequestLog
@Slf4j
public class FileStorageController {

    @Resource
    private FileStorageService fileStorageService;
    @Resource
    private LocalMediaService localMediaService;
    @Resource
    private LocalMediaTagService localMediaTagService;
    @Resource
    private UserService userService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件接口")
    @AuthCheck(needPlatform = true, needToken = true, authLevel = AuthLevel.ADMIN)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String url = fileStorageService.uploadFile(file);
        return ResultUtils.success(url);
    }
    @PostMapping("/uploadavatar")
    @Operation(summary = "上传头像接口")
    @AuthCheck(needPlatform = true, needToken = true, authLevel = AuthLevel.USER)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<String> handleAvatarUpload(@RequestParam("file") MultipartFile file) {
        String url = fileStorageService.uploadAvatar(file);
        userService.updateCommonInfo(null,null,url,null,null,null,null);
        return ResultUtils.success(url);
    }

    @PostMapping("/uploadwithtag")
    @Operation(summary = "上传文件接口")
    @AuthCheck(needPlatform = true, needToken = true, authLevel = AuthLevel.ADMIN)
    @Transactional
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<Void> handleFileUpload(@NotNull @RequestParam("file") MultipartFile file,
                                               @NotNull @RequestParam("tagId")String tagId) {
        Long tag = Long.parseLong(tagId);
        String url = fileStorageService.uploadFile(file);
        try {
            localMediaService.addUrl(url,tag);
        }catch (Exception e){
            fileStorageService.deleteFile(url);
            throw new RuntimeException(e);
        }
        return ResultUtils.success(null);
    }

    @PostMapping("/url/upload")
    @Operation(summary = "上传url接口")
    @AuthCheck(needPlatform = true, needToken = true, authLevel = AuthLevel.ADMIN)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<Void> uploadUrl(@Valid @RequestBody MediaUrlUploadDto dto, BindingResult result) {
        localMediaService.addUrl(dto.getUrl(),dto.getUrlTagId());
        return ResultUtils.success(null);
    }

    @PostMapping("/url/delete")
    @Operation(summary = "删除url接口")
    @AuthCheck(needPlatform = true, needToken = true, authLevel = AuthLevel.ADMIN)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<Void> deleteUrl(@Valid @RequestBody MediaUrlDeleteDto dto, BindingResult result) {
        localMediaService.deleteUrl(dto.getUrlId());
        return ResultUtils.success(null);
    }

    @PostMapping("/url/page")
    @Operation(summary = "分页查询URL接口")
    @AuthCheck(needPlatform = true, needToken = true, authLevel = AuthLevel.ADMIN)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<PageResult<LocalMediaVo>> pageUrl(@Valid @RequestBody MediaUrlPageDto dto, BindingResult result) {
        return ResultUtils.success(localMediaService.pageUrl(dto.getTagId(),dto.getCurrent(),dto.getPageSize(),dto.getSortField(),dto.getSortOrder()));
    }

    @PostMapping("/listtags")
    @Operation(summary = "查询所有多媒体标签")
    @AuthCheck(needToken = true,authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<List<LocalMediaTagVo>> listLocalMediaTags(@Valid @RequestBody EmptyDto dto, BindingResult result){
        return ResultUtils.success(localMediaTagService.listTags());
    }

    @PostMapping("/addtag")
    @Operation(summary = "添加多媒体URL标签")
    @AuthCheck(needToken = true,authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<Void> addLocalMediaTag(@Valid @RequestBody MediaTagAddDto dto, BindingResult result){
        localMediaTagService.addTag(dto.getTagName());
        return ResultUtils.success(null);
    }

    @PostMapping("/deletetag")
    @Operation(summary = "删除多媒体URL标签")
    @AuthCheck(needToken = true,authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<Void> deleteLocalMediaTag(@Valid @RequestBody MediaTagDeleteDto dto, BindingResult result){
        localMediaTagService.deleteTag(dto.getId());
        return ResultUtils.success(null);
    }

}
