package com.liferay.support.tools.portlet.actions;

import com.liferay.frontend.js.loader.modules.extender.npm.NPMResolver;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.support.tools.constants.LDFPortletKeys;
import com.liferay.support.tools.constants.LDFPortletWebKeys;
import com.liferay.support.tools.utils.CommonUtil;
import com.liferay.support.tools.utils.JqueryResolver;
import com.liferay.support.tools.utils.LodashResolver;
import com.liferay.support.tools.utils.WikiCommons;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Render command for all jsps
 * 
 * @author Yasuyuki Takeo
 */
@Component(
    immediate = true, 
    property = { 
        "javax.portlet.name=" + LDFPortletKeys.LIFERAY_DUMMY_FACTORY,
        "mvc.command.name=" + LDFPortletKeys.ORGANIZATION,
        "mvc.command.name=" + LDFPortletKeys.SITES,
        "mvc.command.name=" + LDFPortletKeys.PAGES,
        "mvc.command.name=" + LDFPortletKeys.USERS,
        "mvc.command.name=" + LDFPortletKeys.WCM,
        "mvc.command.name=" + LDFPortletKeys.DOCUMENTS,
        "mvc.command.name=" + LDFPortletKeys.MB,
        "mvc.command.name=" + LDFPortletKeys.CATEGORY,
        "mvc.command.name=" + LDFPortletKeys.BLOGS,
        // "mvc.command.name=" + LDFPortletKeys.WIKI,
        "mvc.command.name=" + LDFPortletKeys.COMMON
    }, 
    service = MVCRenderCommand.class
)
public class CommonMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {

		// Get Mode
		String mode = ParamUtil.getString(
			renderRequest,
			LDFPortletKeys.MODE,
			LDFPortletKeys.MODE_ORGANIZAION);

		// Carry around mode
		renderRequest.setAttribute(LDFPortletKeys.MODE, mode);
		
//		// Component libralies
//		renderRequest.setAttribute(LDFPortletWebKeys.WIKI_COMMONS, _wikiCommons);
//
		if(_log.isDebugEnabled()) {
			_log.debug("mode <" + mode + ">");
			_log.debug("jsp  <" + _commonUtil
			.getPageFromMode()
			.getOrDefault(mode, LDFPortletKeys.JSP_ORGANIZAION) + ">");
		}

        //Loading Lodash
        LodashResolver.exec(renderRequest, _npmResolver);
		//Loading JQuery
		JqueryResolver.exec(renderRequest, _npmResolver);

		return _commonUtil
				.getPageFromMode()
				.getOrDefault(mode, LDFPortletKeys.JSP_ORGANIZAION);
	}

	@Reference(bind = "-")
	private CommonUtil _commonUtil;
	
//	@Reference(bind = "-")
//	private WikiCommons _wikiCommons;
//
    @Reference
    private NPMResolver _npmResolver;

	private static Log _log = LogFactoryUtil
			.getLog(CommonMVCRenderCommand.class);	
}
