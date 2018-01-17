package qjm.rpc.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import qjm.rpc.common.RpcRequest;
import qjm.rpc.common.RpcResponse;

/**
 * 链接服务器调用服务
 * @author QJM
 *
 */
public class RpcClient {
	
	public Object start(RpcRequest request, String host, int port) throws Throwable{
		Socket server = new Socket(host, port);
		
		InputStream in = null;
		ObjectInputStream oin = null;
		OutputStream out = null;
		ObjectOutputStream oout = null;
		try {
			// 1. 发送请求数据
			out = server.getOutputStream();
			oout = new ObjectOutputStream(out);
			oout.writeObject(request);
			oout.flush();
			
			// 2. 获取返回数据，强转参数类型
			in = server.getInputStream();
			oin = new ObjectInputStream(in);
			Object res = oin.readObject();
			RpcResponse response = null;
			if(!(res instanceof RpcResponse)){
				throw new RuntimeException("返回参数不正确");
			}else{
				response = (RpcResponse) res;
			}
			
			// 3. 返回结果
			if(response.getError() != null){ //服务器产生异常
				throw response.getError();
			}
			return response.getResult();
		}finally{
			try {	//关闭流
				if(in != null) in.close();
				if(oin != null) oin.close();
				if(out != null) out.close();
				if(oout != null) oout.close();
				if(server != null) server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
