import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.RequestParam

data class TaskPageRequest(
    @RequestParam(value = "size", defaultValue = "10", required = false)
    val size: Int = 10,

    @RequestParam(value = "page", defaultValue = "0", required = false)
    val page: Int = 0,

    @RequestParam(value = "archive", defaultValue = "false", required = false)
    val archive: Boolean = false
){
    fun toPageable(): Pageable = PageRequest.of(page, size)
}