package cn.com.open.openpaas.userservice.app.oauth.service;

import org.springframework.security.oauth2.provider.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * Add  <i>archived = 0</i> condition
 *
 * 
 */
public class CustomJdbcClientDetailsService extends JdbcClientDetailsService {
	
	private static final String CLIENT_FIELDS_FOR_UPDATE = "resourceIds, scope, "
			+ "authorizedGrantTypes, webServerRedirectUri, authorities, accessTokenValidity, "
			+ "refreshTokenValidity, additionalInformation, icon";
	
	private static final String CLIENT_FIELDS = "appsecret, " + CLIENT_FIELDS_FOR_UPDATE;
	
	private static final String BASE_FIND_STATEMENT = "select appkey, " + CLIENT_FIELDS
	+ " from app ";
	
	private static final String DELETE_STATEMENT_SQL = "delete from app where appkey = ?";
	
	private static final String FIND_STATEMENT_SQL = BASE_FIND_STATEMENT + " order by appkey";
	
	private static final String UPDATE_STATEMENT_SQL = "update app " + "set "
	+ CLIENT_FIELDS_FOR_UPDATE.replaceAll(", ", "=?, ") + "=? where appkey = ?";
	
	private static final String UPDATE_SECRET_STATEMENT_SQL = "update app "
		+ "set appsecret = ? where appkey = ?";
	
	private static final String INSERT_STATEMENT_SQL = "insert into app (" + CLIENT_FIELDS
	+ ", appkey) values (?,?,?,?,?,?,?,?,?,?)";
	
    private static final String SELECT_CLIENT_DETAILS_SQL =  BASE_FIND_STATEMENT + " where appkey = ? and archived = 0 ";

    public CustomJdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
        setDeleteClientDetailsSql(DELETE_STATEMENT_SQL);
        setFindClientDetailsSql(FIND_STATEMENT_SQL);
        setUpdateClientDetailsSql(UPDATE_STATEMENT_SQL);
        setUpdateClientSecretSql(UPDATE_SECRET_STATEMENT_SQL);
        setInsertClientDetailsSql(INSERT_STATEMENT_SQL);
        setSelectClientDetailsSql(SELECT_CLIENT_DETAILS_SQL);
    }
}