package org.njgzr.security;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.njgzr.security.event.interfaces.ConfigGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Mr Gu<admin@njgzr.org>
 * @version Dec 9, 2019 , 3:02:42 PM
 * Description
 */
@Service
@Slf4j
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RedisService {
	
	private static String redisCode = "utf-8";
	
	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	public long del(final String... keys) {
		return (long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				long result = 0;
				for (int i = 0; i < keys.length; i++) {
					result = connection.del(keys[i].getBytes());
				}
				return result;
			}
		});
	}


	/**
	 * 添加key value 并且设置存活时间
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 *            单位毫秒
	 */
	public void set(String key, String value, long liveTime) {
		redisTemplate.opsForValue().set(key, value,liveTime,TimeUnit.MILLISECONDS);
	}

	/**
	 * 添加key value
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		this.set(key, value, 0L);
	}

	/**
	 * 获取redis value (String)
	 * 
	 * @param key
	 * @return
	 */
	public String get(final String key) {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					return new String(connection.get(key.getBytes()), redisCode);
				} catch (UnsupportedEncodingException e) {

					log.error("从redis获取"+key+"失败",e);
				}
				return "";
			}
		});
	}

	/**
	 * 通过正则匹配keys
	 * 
	 * @param pattern
	 * @return
	 * @return
	 */
	public Set<String> Setkeys(String pattern) {
		return redisTemplate.keys(pattern);

	}

	/**
	 * 返回列表中指定范围的元素
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> listRange(String key, long start, long end) {
		return redisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * 删除列表中指定范围的元素
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> listTrim(String key, long start, long end) {
		redisTemplate.opsForList().trim(key, start, end);
		return listRange(key, 0, -1);
	}

	/**
	 * 返回列表的长度
	 * 
	 * @param key
	 * @return
	 */
	public Long listSize(String key) {
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * 将列表指定位置的元素设为新值
	 * 
	 * @param key
	 * @param index
	 * @param value
	 * @return
	 */
	public List<String> listSetIndex(String key, long index, String value) {
		redisTemplate.opsForList().set(key, index, value);
		return listRange(key, 0, -1);
	}

	/**
	 * 获取列表指定位置的元素
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public String listIndex(String key, long index) {
		return redisTemplate.opsForList().index(key, index);
	}

	/**
	 * 删除第一个等于指定值的元素 count> 0：删除等于从头到尾移动的值的元素 count <0：删除等于从尾到头移动的值的元素 count =
	 * 0：删除等于value的所有元素
	 * 
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 */
	public List<String> listRemove(String key, long count, String value) {
		redisTemplate.opsForList().remove(key, count, value);
		return listRange(key, 0, -1);
	}

	/**
	 * 向List头部追加记录
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return 记录总数
	 */
	public long leftPush(String key, String obj) {
		return redisTemplate.opsForList().leftPush(key, obj);// 从左向右存压栈
	}

	/**
	 * 批量向列表头部添加记录
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public long leftPushAll(String key, List<String> values) {
		return redisTemplate.opsForList().leftPushAll(key, values);
	}

	/**
	 * 向List尾部追加记录
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return 记录总数
	 */
	public long rightPush(String key, String obj) {
		return redisTemplate.opsForList().rightPush(key, obj);
	}
	
	public Set<String> getKeys(String pattern){
		return redisTemplate.keys("*" + pattern + "*");
	}

	/**
	 * 批量向列表尾部添加记录
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public long rightPushAll(String key, List<String> values) {
		return redisTemplate.opsForList().rightPushAll(key, values);
	}

	public Object leftPop(String key) {
		return redisTemplate.opsForList().leftPop(key);// 从左出栈
	}

	public Object rightPop(String key) {
		return redisTemplate.opsForList().rightPop(key, 30, TimeUnit.SECONDS);// 从右出栈
	}

	public Long getKeySize(String key) {
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * 检查key是否已经存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 缓存List数据
	 * 
	 * @param key
	 *            缓存的键值
	 * @param dataList
	 *            待缓存的List数据
	 * @return 缓存的对象
	 */
	public Object setCacheList(String key, List<String> dataList) {
		ListOperations<String, String> listOperation = redisTemplate.opsForList();
		if (null != dataList) {
			int size = dataList.size();
			for (int i = 0; i < size; i++) {
				listOperation.rightPush(key, dataList.get(i));
			}
		}
		return listOperation;
	}

	/**
	 * 获得缓存的list对象
	 * 
	 * @param key
	 *            缓存的键值
	 * @return 缓存键值对应的数据
	 */
	public List<String> getCacheList(String key) {
		List<String> dataList = new ArrayList<String>();
		ListOperations<String, String> listOperation = redisTemplate.opsForList();
		Long size = listOperation.size(key);

		for (int i = 0; i < size; i++) {
			dataList.add(listOperation.leftPop(key));
		}
		return dataList;
	}
	
	/**
	 * 根据 key 和属性查询 hash值
	 * @param key
	 * @param attribute
	 * @return 可能为null
	 */
	public String getHash(String key,String attribute) {
		HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
		String object = opsForHash.get(key, attribute);
		return object;
	}
	
	/**
	 * 清空redis 所有数据
	 * 
	 * @return
	 */
	public String flushDB() {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	}

	/**
	 * 查看redis里有多少数据
	 * 
	 * @return
	 */
	public long dbSize() {
		return (long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.dbSize();
			}
		});
	}

	/**
	 * 检查是否连接成功
	 * 
	 * @return
	 */
	public String ping() {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.ping();
			}
		});
	}
	
	/**
	 * 关闭连接
	 * @return
	 */
	public void close() {
		redisTemplate.execute(new RedisCallback() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				 connection.close();
				 System.out.println(connection.isClosed());
				 return true;
			}
		});
	}
	
	public void getClient() {
		List<RedisClientInfo> res = redisTemplate.getClientList();
		for (RedisClientInfo redisClientInfo : res) {
			System.out.println(redisClientInfo.getName());
		}
	}
	
	public String cachePutIfAbsent(String key,String value) {
		if(redisTemplate.opsForValue().setIfAbsent(key, value))
			return value;
		return redisTemplate.opsForValue().get(key);
	}
	
	public String cacheGet(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	public void removeKey(String key) {
		redisTemplate.delete(key);
	}
	
	private StringRedisTemplate redisTemplate;
	
	@PostConstruct
	public void gerRedisService() {
		this.redisTemplate = configGetService.getStringRedisTemplate();
	}
	
	@Autowired
    private ConfigGetService configGetService;
	
}
