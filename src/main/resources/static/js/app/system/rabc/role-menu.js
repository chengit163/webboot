var roleId;
var roleMenuTree;
var oldMenuIds;

$(function () {
    roleId = systemRoleDialog.find('input[name="id"]').val();
    $.ajax({
        url: '/system/rabc/getRoleMenu',
        type: 'POST',
        data: {'roleId': roleId},
        success: function (json) {
            systemRoleDialog.find('#roleMenuLoading').remove();
            roleMenuTree = $.fn.zTree.init($("#roleMenuTree"), {
                check: {
                    enable: true,
                    chkStyle: "checkbox",
                    chkboxType: {"Y": "p", "N": "s"}
                },
                data: {
                    simpleData: {
                        enable: false,
                        idKey: "id",
                        pidKey: "pid"
                    }
                }
            }, json);
            //
            var nodes = roleMenuTree.getNodes();
            for (var i = 0; i < nodes.length; i++) {
                roleMenuTree.expandNode(nodes[i], true, false, true);
            }
            oldMenuIds = getRoleMenuIds();
        }, error: function () {
            systemRoleDialog.find('#roleMenuLoading').remove();
        }
    });
});

function saveRoleMenuPermission() {
    if (roleMenuTree != undefined) {
        var menuIds = getRoleMenuIds();
        if (menuIds.toString() != oldMenuIds.toString()) {
            systemRoleDialog.modalDialogShowLoading();
            $.ajax({
                url: '/system/rabc/saveRoleMenu',
                type: 'POST',
                data: {'roleId': roleId, 'menuIds': menuIds},
                success: function (json) {
                    if (json.code == 0) {
                        oldMenuIds = menuIds;
                        // systemRoleDialog.modalDialogHideLoading();
                        systemRoleDialog.modal('hide');
                        toastr.success(json.msg);
                    } else {
                        systemRoleDialog.modalDialogHideLoading();
                        toastr.error('msg: ' + json.msg, 'code: ' + json.code);
                    }
                }, error: function () {
                    systemRoleDialog.modalDialogHideLoading();
                    toastr.error('Server Error');
                }
            });
        } else {
            toastr.warning('无变化');
        }
    }
}

function getRoleMenuIds() {
    var ids = new Array();
    if (roleMenuTree != undefined) {
        var nodes = roleMenuTree.getCheckedNodes();
        $.each(nodes, function (i, node) {
            ids[i] = node['id'];
        });
    }
    return ids;
}