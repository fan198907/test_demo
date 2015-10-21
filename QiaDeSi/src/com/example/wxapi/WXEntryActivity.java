/**
 * 
 */
package com.example.wxapi;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class WXEntryActivity implements IWXAPIEventHandler{
	
	private Button btnShare;private IWXAPI wxApi;
	private String apiAPPID = "wx2116244240bf98d3";

	/* (non-Javadoc)
	 * @see com.tencent.mm.sdk.openapi.IWXAPIEventHandler#onReq(com.tencent.mm.sdk.modelbase.BaseReq)
	 */
	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.tencent.mm.sdk.openapi.IWXAPIEventHandler#onResp(com.tencent.mm.sdk.modelbase.BaseResp)
	 */
	@Override
	public void onResp(BaseResp resp) {
		String result;
        switch (resp.errCode) {
        case BaseResp.ErrCode.ERR_OK:
            result = "发送成功";
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL:
            result = "发送取消";
            break;
        case BaseResp.ErrCode.ERR_AUTH_DENIED:
            result = "发送被拒绝";
            break;
        default:
            result = "发送返回";
            break;
        }
        System.out.println(result);
		
	}

}
