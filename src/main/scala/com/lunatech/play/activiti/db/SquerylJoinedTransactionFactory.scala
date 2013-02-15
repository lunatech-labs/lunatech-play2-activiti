package com.lunatech.play.activiti.db
import java.sql.Connection
import java.util.Properties
import javax.sql.DataSource
import org.apache.ibatis.session.TransactionIsolationLevel
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransaction
import org.apache.ibatis.transaction.managed.ManagedTransaction
import org.squeryl.Session

/**
 * Transaction factory that joins a Squeryl transaction if called from inside a Squeryl transaction block.
 *
 * Limitation: assumes that there is only one data source.
 */
class SquerylJoinedTransactionFactory extends TransactionFactory {
  override def setProperties(props: Properties) {}

  override def newTransaction(connection: Connection) = {
    Session.currentSessionOption map { session =>
      if (session.connection == connection)
        new ManagedTransaction(connection, false)
      else
        new JdbcTransaction(connection)
    } getOrElse new JdbcTransaction(connection)
  }

  def newTransaction(dataSource: DataSource, level: TransactionIsolationLevel, autoCommit: Boolean) = {
    Session.currentSessionOption map { session =>
      new ManagedTransaction(session.connection, false)
    } getOrElse new JdbcTransaction(dataSource, level, true)
  }

}