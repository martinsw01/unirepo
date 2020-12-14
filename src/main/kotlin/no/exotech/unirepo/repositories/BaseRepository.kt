package no.exotech.unirepo.repositories

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

open class BaseRepository(className: String, url: String, user: String, psw: String) {
    protected val connection: Connection

    init {
        Class.forName(className)
        connection = DriverManager.getConnection(url, user, psw)
    }

    fun <T> actOnPreparedStatement(prepStm: PreparedStatement, callback: (prepStm: PreparedStatement) -> T): T {
        val result: T
        try {
            result = callback(prepStm)
        } catch (e: Exception) {
            prepStm.close()
            throw e
        }
        prepStm.close()
        return result
    }

    fun <T> actOnResultSet(rs: ResultSet, callback: (rs: ResultSet) -> T): T {
        val result: T
        try {
            result = callback(rs)
        } catch (e: Exception) {
            rs.close()
            throw e
        }
        rs.close()
        return result
    }
}