package demo.usul.repository

import demo.usul.entity.ColumnEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ColumnRepository : CrudRepository<ColumnEntity, UUID> {

    @Query(nativeQuery = true, value = "select * from cols limit 1")
    fun findFromView(): List<ColumnEntity>?
}