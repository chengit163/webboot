var Regex =
    {
        //帐号是否合法(字母开头，允许5-16字节，允许字母数字下划线)：^[a-zA-Z][a-zA-Z0-9_]{4,15}$
        //密码(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)：^[a-zA-Z]\w{5,17}$
        //强密码(必须包含大小写字母和数字的组合，不能使用特殊字符，长度在8-10之间)：^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$
    };

var Validator =
    {

        /**非空验证*/
        isNotNull: function (source) {
            return source != null && source != undefined && source != "";
        },
        /**去空格后非空验证*/
        isNotNullTrim: function (source) {
            return source != null && source != undefined && $.trim(source) != "";
        },
    };