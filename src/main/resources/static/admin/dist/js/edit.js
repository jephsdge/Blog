var blogEditor;
// Tags Input
$('#blogTag').tagsInput({
    width: '100%',
    height: '38px',
    defaultText: '文章标签'
});

//Initialize Select2 Elements
$('.select2').select2()

$(function () {
    blogEditor = editormd("blog-editormd", {
        width: "100%",
        height: 640,
        syncScrolling: "single",
        path: "/admin/plugins/editormd/lib/",
        toolbarModes: 'full',
        /**图片上传配置*/
        imageUpload: true,
        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"], //图片上传格式
        imageUploadURL: "/admin/blogs/md/uploadfile",
        onload: function (obj) { //上传成功之后的回调
        }
    });

    // 编辑器粘贴上传
    document.getElementById("blog-editormd").addEventListener("paste", function (e) {
        var clipboardData = e.clipboardData;
        if (clipboardData) {
            var items = clipboardData.items;
            if (items && items.length > 0) {
                for (var item of items) {
                    if (item.type.startsWith("image/")) {
                        var file = item.getAsFile();
                        if (!file) {
                            alert("请上传有效文件");
                            return;
                        }
                        var formData = new FormData();
                        formData.append('file', file);
                        var xhr = new XMLHttpRequest();
                        xhr.open("POST", "/admin/upload/file");
                        xhr.onreadystatechange = function () {
                            if (xhr.readyState == 4 && xhr.status == 200) {
                                var json=JSON.parse(xhr.responseText);
                                if (json.resultCode == 200) {
                                    blogEditor.insertValue("![](" + json.data + ")");
                                } else {
                                    alert("上传失败");
                                }
                            }
                        }
                        xhr.send(formData);
                    }
                }
            }
        }
    });

    new AjaxUpload('#uploadCoverImage', {
        action: '/admin/upload/file',
        name: 'file',
        autoSubmit: true,
        responseType: 'json',
        onSubmit: function (file, extension) {
            if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))) {
                alert('只支持jpg、png、gif格式的文件！');
                return false;
            }
        },
        onComplete: function (file, json) {
            if (json != null && json.code === 200) {
                console.log(json)
                $("#blogCoverImage").attr("src", json.data);
                $("#blogCoverImage").attr("style", "width: 128px;height: 128px;display:block;");
                return false;
            } else {
                alert("error");
            }
        }
    });
});

$('#confirmButton').click(function () {
    var blogTitle = $('#blogTitle').val();
    var blogSubTitle = $('#blogSubTitle').val();
    var blogCategoryId = $('#blogCategoryId').val();
    var blogTag = $('#blogTag').val();
    var blogContent = blogEditor.getMarkdown();
    if (isNull(blogTitle)) {
        swal("请输入文章标题", {
            icon: "error",
        });
        return;
    }
    if (!validLength(blogTitle, 150)) {
        swal("标题过长", {
            icon: "error",
        });
        return;
    }
    if (!validLength(blogSubTitle, 150)) {
        swal("副标题过长", {
            icon: "error",
        });
        return;
    }
    if (isNull(blogCategoryId)) {
        swal("请选择文章分类", {
            icon: "error",
        });
        return;
    }
    if (isNull(blogTag)) {
        swal("请输入文章标签", {
            icon: "error",
        });
        return;
    }
    if (!validLength(blogTag, 150)) {
        swal("标签过长", {
            icon: "error",
        });
        return;
    }
    if (isNull(blogContent)) {
        swal("请输入文章内容", {
            icon: "error",
        });
        return;
    }
    if (!validLength(blogContent, 100000)) {
        swal("文章内容过长", {
            icon: "error",
        });
        return;
    }
    $('#articleModal').modal('show');
});

$('#saveButton').click(function () {
    var blogId = $('#blogId').val();
    var blogTitle = $('#blogTitle').val();
    var blogSubTitle = $('#blogSubTitle').val();
    var blogCategoryId = $('#blogCategoryId').val();
    var blogTag = $('#blogTag').val();
    var blogContent = blogEditor.getMarkdown();
    var blogCoverImage = $('#blogCoverImage')[0].src;
    // console.log(blogCoverImage.replace(/http[s]?:\/\/[^\/]+/,''))
    blogCoverImage = blogCoverImage.replace(/http[s]?:\/\/[^\/]+/,'')
    var blogStatus = $("input[name='blogStatus']:checked").val();
    var enableComment = $("input[name='enableComment']:checked").val();
    if (isNull(blogCoverImage) || blogCoverImage.indexOf('img-upload') != -1) {
        swal("封面图片不能为空", {
            icon: "error",
        });
        return;
    }
    var url = '/admin/blogs/save';
    var swlMessage = '保存成功';
    var data = {
        "blogId": blogId,
        "blogTitle": blogTitle,
        "blogSubTitle": blogSubTitle,
        "blogCategoryId": blogCategoryId,
        "blogTag": blogTag,
        "blogContent": blogContent,
        "blogImg": blogCoverImage,
        "blogStatus": blogStatus,
        "enableComment": enableComment
    };
    /*if (blogId > 0) {
        url = '/admin/blogs/update';
        swlMessage = '修改成功';
        data = {
            "blogId": blogId,
            "blogTitle": blogTitle,
            "blogSubTitle": blogSubTitle,
            "blogCategoryId": blogCategoryId,
            "blogTags": blogTag,
            "blogContent": blogContent,
            "blogCoverImage": blogCoverImage,
            "blogStatus": blogStatus,
            "enableComment": enableComment
        };
    }*/
    $.ajax({
        type: 'POST',//方法类型
        url: url,
        data: data,
        datatype: "json",
        success: function (result) {
            if (result.code === 200) {
                $('#articleModal').modal('hide');
                swal({
                    title: swlMessage,
                    type: 'success',
                    showCancelButton: false,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '返回博客列表',
                    confirmButtonClass: 'btn btn-success',
                    buttonsStyling: false
                }).then(function () {
                    window.location.href = "/admin/blogs";
                })
            }
            else {
                $('#articleModal').modal('hide');
                swal(result.message, {
                    icon: "error",
                });
            }
            ;
        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });
});

function getBoolean (param){
    if (param === 1){
        return true;
    }else if (param === 0){
        return false;
    }
}

$('#cancelButton').click(function () {
    window.location.href = "/admin/blogs";
});

/**
 * 随机封面功能
 */
$('#randomCoverImage').click(function () {
    var rand = parseInt(Math.random() * 40 + 1);
    $("#blogCoverImage").attr("src", '/admin/dist/img/rand/' + rand + ".jpg");
    $("#blogCoverImage").attr("style", "width:160px ;height: 120px;display:block;");
});
