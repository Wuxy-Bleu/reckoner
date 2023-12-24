package demo.usul.service

import demo.usul.exceptions.DbElementNotFoundException
import demo.usul.model.ColumnEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection

class SelectFromCols(private val connection: Connection) {

    companion object {
        private const val SELECT_FROM_COLS = "SELECT * FROM cols"
    }

//    suspend fun read(id: Int): ColumnEntity = withContext(Dispatchers.IO) {
//
//        val statement = connection.prepareStatement(SELECT_FROM_COLS)
//        statement.setInt(1, id)
//        val resultSet = statement.executeQuery()
//
//        if (resultSet.next()) {
//            val title = resultSet.getString("title")
//            val body = resultSet.getString("body")
////            return@withContext Article(title, body)
//            return ColumnEntity()
//        } else {
//            throw DbElementNotFoundException("Record not found")
//        }
//    }

}