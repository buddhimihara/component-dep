package com.wso2telco.dep.billingservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.billingservice.util.DatabaseTables;

public class RateCardDAO {

	/** The Constant log. */
	private static final Log log = LogFactory.getLog(RateCardDAO.class);

//TODO:change it here
	/*
	* select apiop.api_operationid, apiop.api_operation, apiop.api_operationcode
	from api_operation apiop, api
	where apiop.apiid = api.apiid
	and api.apiname='smsmessaging'

	* */
	public Map<Integer, String> getServiceDetailsByAPICode(String apiCode) throws SQLException, Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<Integer, String> serviceDetails = new HashMap<Integer, String>();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			/*StringBuilder queryString = new StringBuilder("SELECT services.servicesDid, services.code ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.IN_MD_API.getTObject()); //inmdapi
			queryString.append(" api, ");
			queryString.append(DatabaseTables.IN_MD_SERVICES.getTObject()); //inmdservices
			queryString.append(" services ");
			queryString.append("WHERE api.apiDid = services.apiDid ");
			queryString.append("AND api.code = ?");
			*/

			StringBuilder query = new StringBuilder("select apiop.api_operationid, apiop.api_operation, apiop.api_operationcode ");
			query.append("from api_operation apiop, api ");
			query.append("where apiop.apiid = api.apiid ");
			query.append("and api.apiname=?");

			//ps = con.prepareStatement(queryString.toString());
			ps = con.prepareStatement(query.toString());

			ps.setString(1, apiCode);

			log.debug("sql query in getServiceDetailsByAPICode : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				//serviceDetails.put(rs.getInt("servicesDid"), rs.getString("code"));
				serviceDetails.put(rs.getInt("api_operationid"), rs.getString("api_operation"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getServiceDetailsByAPICode : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in getServiceDetailsByAPICode : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return serviceDetails;
	}

	//TODO:correct this -- done

	/*
	select opr.operation_rateid,rd.rate_defname
from operation_rate opr, rate_def rd
where opr.rate_defid=rd.rate_defid
and api_operationid=1
	* */
	public Map<Integer, String> getHubRateDetailsByServicesDid(int servicesDid) throws SQLException, Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<Integer, String> rateDetails = new HashMap<Integer, String>();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			/*
			StringBuilder queryString = new StringBuilder("SELECT operationRate.servicesRateDid, rate.code ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.IN_MD_RATE.getTObject());  //inmdrate
			queryString.append(" rate, ");
			queryString.append(DatabaseTables.IN_OPERATION_RATE.getTObject()); //inmdoperationrate
			queryString.append(" operationRate ");
			queryString.append("WHERE operationRate.rateDid = rate.rateDid ");
			queryString.append("AND operationRate.servicesDid = ?");
			*/

			StringBuilder query = new StringBuilder("select opr.operation_rateid,rd.rate_defname ");
			query.append("from operation_rate opr, rate_def rd ");
			query.append("where opr.rate_defid=rd.rate_defid ");
			query.append("and api_operationid=? ");

			ps = con.prepareStatement(query.toString());

			ps.setInt(1, servicesDid);

			log.debug("sql query in getHubRateDetailsByServicesDid : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				rateDetails.put(rs.getInt("operation_rateid"), rs.getString("rate_defname"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getHubRateDetailsByServicesDid : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in getHubRateDetailsByServicesDid : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateDetails;
	}

	//TODO:changing
	public Map<Integer, String> getOperatorRateDetailsByServicesDidAndOperatorCode(int servicesDid, String operatorCode)
			throws SQLException, Exception {

		Connection con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<Integer, String> rateDetails = new HashMap<Integer, String>();

		try {

			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder queryString = new StringBuilder("SELECT operatorRate.operatorRateDid, rate.code ");
			queryString.append("FROM ");
			queryString.append(DatabaseTables.IN_MD_RATE.getTObject()); //inmdrate
			queryString.append(" rate, ");
			queryString.append(DatabaseTables.IN_MD_OPERATOR.getTObject());
			queryString.append(" operator, ");
			queryString.append(DatabaseTables.IN_MD_OPERATOR_RATE.getTObject()); //inmdoperatorrate
			queryString.append(" operatorRate ");
			queryString.append("WHERE operatorRate.rateDid = rate.rateDid ");
			queryString.append("AND operator.operatorDid = operatorRate.operatorDid ");
			queryString.append("AND operatorRate.servicesDid = ? ");
			queryString.append("AND operator.code = ?");

			ps = con.prepareStatement(queryString.toString());

			ps.setInt(1, servicesDid);
			ps.setString(2, operatorCode);

			log.debug("sql query in getOperatorRateDetailsByServicesDidAndOperatorCode : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				rateDetails.put(rs.getInt("operatorRateDid"), rs.getString("code"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getOperatorRateDetailsByServicesDidAndOperatorCode : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in getOperatorRateDetailsByServicesDidAndOperatorCode : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateDetails;
	}


	//setters - TODO:change here rate. write rollback as well
	/*
	INSERT INTO rate_db.sub_rate_nb
	(api_operationid,applicationid,rate_defid)
	VALUES (9,3,(SELECT rate_defid
	FROM rate_def WHERE rate_defname='SM1'))
	*/
	//public void setHubSubscriptionRateData(int servicesRateDid, int applicationDid, String apiCode)
	public void setHubSubscriptionRateData(int servicesRateDid, int applicationDid, String rate)
			throws SQLException, Exception {

		Connection conn = null;
		PreparedStatement ps = null;

		try {

			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			/*
			StringBuilder query = new StringBuilder();
			query.append("INSERT INTO ");
			query.append(DatabaseTables.IN_MD_NB_SUBSCRIPTION_RATE.getTObject()); //inmdnbsubscriptionrate
			query.append(" (servicesRateDid, applicationDid, apiDid) ");
			query.append("VALUES (?, ?, (SELECT apiDid ");
			query.append("FROM ");
			query.append(DatabaseTables.IN_MD_API.getTObject()); //inmdapi
			query.append(" WHERE code = ?))");

			ps = conn.prepareStatement(query.toString());

			ps.setInt(1, servicesRateDid);
			ps.setInt(2, applicationDid);
			ps.setString(3, apiCode);
			*/
			StringBuilder query = new StringBuilder("INSERT INTO rate_db.sub_rate_nb ");
			query.append("(api_operationid,applicationid,rate_defid) ");
			query.append("VALUES (?,?,");
			query.append("(SELECT rate_defid FROM rate_def WHERE rate_defname=?))");

			ps = conn.prepareStatement(query.toString());
			ps.setInt(1,servicesRateDid);
			ps.setInt(2,applicationDid);
			ps.setString(3,rate);

			log.debug("sql query in setHubSubscriptionRateData : " + ps);

			ps.execute();
		} catch (SQLException e) {

			log.error("database operation error in setHubSubscriptionRateData : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in setHubSubscriptionRateData : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, conn, null);
		}
	}


	//TODO:change this - get values
	//public void setOperatorSubscriptionRateData(int operatorRateDid, int applicationDid)
	public void setOperatorSubscriptionRateData(int operatorRateDid, int applicationDid, String operatorId, String operationId)
			throws SQLException, Exception {

		Connection conn = null;
		PreparedStatement ps = null;

		try {

			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			/*
			StringBuilder query = new StringBuilder();
			query.append("INSERT INTO ");
			query.append(DatabaseTables.IN_MD_SB_SUBSCRIPTIONS.getTObject());
			query.append(" (operationRateDid, applicationDid) ");
			query.append("VALUES (?, ?)");
			*/

			StringBuilder query = new StringBuilder("NSERT INTO sub_rate_sb ");
			query.append("(operatorid, api_operationid, applicationid, rate_defid)");
			query.append("VALUES (?, ?,?,?)");

			ps = conn.prepareStatement(query.toString());

			ps.setInt(1, 1);
			ps.setInt(2, 1);
			ps.setInt(3,1);
			ps.setInt(4,2);

			log.debug("sql query in setOperatorSubscriptionRateData : " + ps);

			ps.execute();
		} catch (SQLException e) {

			log.error("database operation error in setOperatorSubscriptionRateData : ", e);
			throw e;
		} catch (Exception e) {

			log.error("error in setOperatorSubscriptionRateData : ", e);
			throw e;
		} finally {

			DbUtils.closeAllConnections(ps, conn, null);
		}
	}
}
