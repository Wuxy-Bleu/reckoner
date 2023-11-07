package demo.usul.entity

import demo.usul.annotation.NoArgs
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.Immutable
import java.util.*

@Entity
@NoArgs
@Immutable
class ColumnEntity(
    @Id
    var id: UUID? = UUID.randomUUID(),
    var tableCatalog: String?,
    var tableSchema: String?,
    var tableName: String?,
    var columnName: String?,
    var isPrimaryKey: Boolean?,
    var ordinalPosition: Int?,
    var columnDefault: String?,
    var isNullable: Boolean? = false,
    var dataType: String?,
    var characterMaximumLength: Int?,
    var characterOctetLength: Long?,
    var numericPrecision: Int?,
    var numericPrecisionRadix: Int?,
    var numericScale: Int?,
    var datetimePrecision: Int?,
    var intervalType: String?,
    var intervalPrecision: String?,
    var characterSetCatalog: String?,
    var characterSetSchema: String?,
    var characterSetName: String?,
    var collationCatalog: String?,
    var collationSchema: String?,
    var collationName: String?,
    var domainCatalog: String?,
    var domainSchema: String?,
    var domainName: String?,
    var udtCatalog: String?,
    var udtSchema: String?,
    var udtName: String?,
    var scopeCatalog: String?,
    var scopeSchema: String?,
    var scopeName: String?,
    var maximumCardinality: String?,
    var dtdIdentifier: Int?,
    var isSelfReferencing: Boolean?,
    var isIdentity: Boolean?,
    var identityGeneration: String?,
    var identityStart: String?,
    var identityIncrement: String?,
    var identityMaximum: String?,
    var identityMinimum: String?,
    var identityCycle: Boolean?,
    var isGenerated: String?,
    var generationExpression: String?,
    var isUpdatable: Boolean?
)