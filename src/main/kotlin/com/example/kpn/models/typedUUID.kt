import java.util.UUID

@Suppress("unused")
@JvmInline
value class UUID<TargetType>(val value: UUID) {
  companion object {
    @JvmStatic fun <TargetType> fromString(name: String) = UUID<TargetType>(UUID.fromString(name))
    @JvmStatic fun <TargetType> randomUUID() = UUID<TargetType>(UUID.randomUUID())
  }
}
