package qjm.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import qjm.rpc.common.RpcRequest;

/**
 * 客户端服务类代理工厂
 * 
 * 1. 调用getProxy方法获取代理对象
 * 2. 代理对象的方法被调用时会被invoke方法拦截，执行invoke
 * 	 	1）封装参数，用于发送到服务器，定位服务、执行服务
 * 		2）链接服务器调用服务
 * @author QJM
 *
 */
public class RpcClientProxy implements InvocationHandler{
	private String host;
	private int port;
	
	public RpcClientProxy(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	/**
	 * 生成代理对象
	 * @param clazz 代理类型（接口）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T>T getProxy(Class<T> clazz){
		// clazz不是接口不能使用JDK动态代理
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, RpcClientProxy.this);
	}

	public Object invoke(Object obj, Method method, Object[] params) throws Throwable {
		//封装参数
		RpcRequest request = new RpcRequest();
		request.setClassName(method.getDeclaringClass().getName());
		request.setMethodName(method.getName());
		request.setParamTypes(method.getParameterTypes());
		request.setParams(params);
		//链接服务器调用服务
		RpcClient client = new RpcClient();
		return client.start(request, host, port);
	}
}
