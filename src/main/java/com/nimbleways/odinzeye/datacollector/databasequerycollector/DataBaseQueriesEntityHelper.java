package com.nimbleways.odinzeye.datacollector.databasequerycollector;



import com.nimbleways.odinzeye.datacollector.services.CurrentRequestIDUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DataBaseQueriesEntityHelper {
    public static DataBaseQueryEntity build(final Object[] args, final String methodName, long executionTime, OffsetDateTime timeStamp, List<String> boundParams, List<String> paramsDbTypes, List<String> paramJavaTypes, boolean isDispatchedFromJPA) {
        final String requestID = CurrentRequestIDUtils.getCurrentRequestID();

        final String sql = args[0].toString();
        final String operation = Arrays.stream(sql.split("\\s+")).toList().get(0);

        String type = switch (methodName) {
            case "prepareStatement" -> "Prepared Statement";
            case "prepareCall" -> "Callable Statement";
            default -> "Undefined Statement Type";
        };

        return new DataBaseQueryEntity(sql,type,null, operation, executionTime, timeStamp, boundParams, paramsDbTypes, paramJavaTypes, isDispatchedFromJPA,requestID,null,null);
    }
    public static void assignTypesToSqlValues(final Object[] stmtArgs, ParameterMetaData paramMetaData, List<String> boundParams, List<String> paramsDbTypes, List<String> paramsJavaTypes) throws SQLException {
        final int paramIndex = Integer.parseInt(stmtArgs[0].toString());
        String paramDbType = paramMetaData.getParameterClassName(paramIndex);
        String paramJavaType = paramMetaData.getParameterTypeName(paramIndex);
        boundParams.add(paramIndex - 1, stmtArgs[1].toString());
        paramsDbTypes.add(paramIndex - 1, paramDbType);
        paramsJavaTypes.add(paramIndex - 1, paramJavaType);
    }
}