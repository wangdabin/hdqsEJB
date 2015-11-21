package com.ceb.hdqs.query.entity;

/**
 * 存储查询业务的授权情况
 * @author user
 *
 */
public class AuthorizeLevel extends SkyPair<Integer, String> {

	
	public AuthorizeLevel(){
		this.key = 0;
		this.value="";
	}
	public AuthorizeLevel(Integer authLevel,String authInfo){
		this.key = authLevel;
		this.value = authInfo; 
	}
	
	@Override
	public Integer getKey() {
		if(key==null){
			return 0;
		}
		return key;
	}
    @Override
    public boolean setKey(Integer key) {
    	if(this.getKey().intValue()<key.intValue()){//只有新的Key大于已有的Key的时候，才会将Key设置为新的Key
    		this.key = key;
    		return true;
    	}
    	return false;
    }
    
}
