package tv.blademaker.kotify.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.PlaylistPagination

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = PlaylistPagination.Item::class)
object ItemListSerializer : KSerializer<List<PlaylistPagination.Item>> {
    private val listSerializer = ListSerializer(PlaylistPagination.Item.serializer())

    /**
     * Deserialize the entire playlist but do not serialize local tracks
     * and prevent to throw exception when a local track is serialized but
     * not includes much of the required fields
     *
     * @since 0.4.5
     */
    override fun deserialize(decoder: Decoder): List<PlaylistPagination.Item> {
        check(decoder is JsonDecoder) {
            "This decoder is only supported with Json"
        }

        return decoder.decodeJsonElement().jsonArray.mapNotNull {
            try {
                Kotify.JSON.decodeFromJsonElement(PlaylistPagination.Item.serializer(), it).takeIf { i -> !i.isLocal }
            } catch (e: SerializationException) {
                null
            }
        }
    }

    override fun serialize(encoder: Encoder, value: List<PlaylistPagination.Item>) {
        check(encoder is JsonEncoder) {
            "This encoder is only supported with Json"
        }

        listSerializer.serialize(encoder, value)
    }
}