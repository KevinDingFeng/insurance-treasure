package com.shenghesun.service.cpic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
  * 异步方法
  * @ClassName: AsyncService 
  * @Description: TODO
  * @author: yangzp
  * @date: 2018年10月10日 下午7:04:32  
  */
@Service
public class AsyncService {
//	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	@Autowired
//	private PayService payService;
	
	@Autowired
//	private WebServiceClient webServiceClient;
	
//	@Autowired
//	private SmsCodeService smsCodeService;
	
	@Async("asyncServiceExecutor")
    public void executeAsync(String orderNo) {
		
	}
	
}
