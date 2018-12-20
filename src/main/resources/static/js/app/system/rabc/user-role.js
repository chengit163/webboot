var userId;
var userRoleTree;
var oldRoleIds;

$(function () {
    userId = systemUserDialog.find('input[name="id"]').val();
    $.ajax({
        url: '/system/rabc/getUserRole',
        type: 'POST',
        data: {'userId': userId},
        success: function (json) {
            systemUserDialog.find('#userRoleLoading').remove();
            userRoleTree = $.fn.zTree.init($("#userRoleTree"), {
                check: {
                    enable: true,
                    chkStyle: "checkbox",
                    chkboxType: {"Y": "p", "N": "s"}
                },
                data: {
                    simpleData: {
                        enable: false,
                        idKey: "id",
                        pidKey: "pid",
                    }
                }
            }, json);
            //
            var nodes = userRoleTree.getNodes();
            for (var i = 0; i < nodes.length; i++) {
                userRoleTree.expandNode(nodes[i], true, false, true);
            }
            oldRoleIds = getUserRoleIds();
        }, error: function () {
            systemUserDialog.find('#userRoleLoading').remove();
        }
    });
});

function saveUserRolePermission() {
    if (userRoleTree != undefined) {
        var roleIds = getUserRoleIds();
        if (roleIds.toString() != oldRoleIds.toString()) {
            systemUserDialog.modalDialogShowLoading();
            $.ajax({
                url: '/system/rabc/saveUserRole',
                type: 'POST',
                data: {'userId': userId, 'roleIds': roleIds},
                success: function (json) {
                    if (json.code == 0) {
                        oldRoleIds = roleIds;
                        // systemUserDialog.modalDialogHideLoading();
                        systemUserDialog.modal('hide');
                        toastr.success(json.msg);
                    } else {
                        systemUserDialog.modalDialogHideLoading();
                        toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                    }
                }, error: function (error) {
                    systemUserDialog.modalDialogHideLoading();
                    toastr.error('Server Error');
                }
            });
        } else {
            toastr.warning('无变化');
        }
    }
}

function getUserRoleIds() {
    var ids = new Array();
    if (userRoleTree != undefined) {
        var nodes = userRoleTree.getCheckedNodes();
        $.each(nodes, function (i, node) {
            ids[i] = node['id'];
        });
    }
    return ids;
}