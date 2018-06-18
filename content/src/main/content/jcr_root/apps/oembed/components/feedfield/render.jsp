<%@include file="/libs/granite/ui/global.jsp" %><%
%><%@page session="false"
          import="org.apache.commons.lang.StringUtils,
                  com.adobe.granite.ui.components.AttrBuilder,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.Field,
                  com.adobe.granite.ui.components.Tag" %><%

    Config cfg = cmp.getConfig();
    ValueMap vm = (ValueMap) request.getAttribute(Field.class.getName());
    Field field = new Field(cfg);

    Tag tag = cmp.consumeTag();
    AttrBuilder attrs = tag.getAttrs();
    cmp.populateCommonAttrs(attrs);

    attrs.add("type", "url");
    attrs.add("name", cfg.get("name", String.class));
    attrs.add("autocomplete", "false");


    attrs.add("value", vm.get("value", String.class));

    if (cfg.get("required", false)) {
        attrs.add("aria-required", true);
    }

    // @coral
    attrs.add("is", "coral-textfield");



	String validatorId = "feedfield-validator-" + System.currentTimeMillis();
%><input <%= attrs.build() %> id="<%=validatorId%>">
<script>
    var validatorId = "<%=validatorId%>";
	console.log(validatorId);
    (function($){
		var $feedField = $("#" + validatorId)[0];

        console.log($feedField);

		$.validator.register({
            selector: $feedField,
            validate: validate
        });

        function validate(){
            if ($feedField.value.match(/^https?:\/\//)) {
                console.log("Looks like a URL");
                if (!$feedField.value.match(/^embed:https?:\/\//)) {
                    $feedField.value = "embed:" + $feedField.value;
                    console.log("Adding embed:");
                }
            }

            return null;
        }
    })(jQuery);
  </script>