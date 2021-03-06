package com.zf.web.controller;

import com.zf.service.CommonService;
import com.zf.service.WebSocketServer;
import com.zf.utils.ResponseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class WebController {

	@RequestMapping(value = "/index")
	public String index(Map<String, Object> map, HttpServletRequest request) throws Exception {
		//String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("cid", CommonService.getIpAddr(request));
		List<Map<String, Object>> list = new ArrayList<>();
		File file = new File("C:\\Users\\zhangfei\\Desktop\\cases");
		File[] cases = file.listFiles();
		for (File tcase : cases) {
			String tcn = tcase.getName();
			String content = this.readFile(tcase);
			Map<String, Object> m = new HashMap<>();
			m.put("testCaseFileName",tcn);
			m.put("testCase",content);
			list.add(m);
		}
		map.put("testcases",list);
		return "welcome";
	}

	//推送数据接口
	@ResponseBody
	@RequestMapping("/socket/push/{cid}")
	public ResponseUtil.ResponseInfo pushToWeb(@PathVariable String cid, String message) {
		try {
			WebSocketServer.sendInfo(message, cid);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseUtil.getFailedResponse(cid + "#" + e.getMessage());
		}
		return ResponseUtil.getSuccessResponse();
	}

	private String readFile(File file) throws Exception {
		StringBuffer sb = new StringBuffer();
		FileInputStream fis = new FileInputStream(file);
		byte[] b = new byte[1024];
		int index;
		while ((index = fis.read(b)) != -1) {
			sb.append(new String(b, 0, index));
		}
		return sb.toString();
	}

}
