import androidx.lifecycle.MutableLiveData
import com.rafabap.flickergallery.presentation.view.NetworkState

fun MutableLiveData<NetworkState>.running(detail: String? = null) {
    this.postValue(NetworkState(NetworkState.NetworkStateStatus.RUNNING, detail))
}

fun MutableLiveData<NetworkState>.success(detail: String? = null) {
    this.postValue(NetworkState(NetworkState.NetworkStateStatus.SUCCESS, detail))
}

fun MutableLiveData<NetworkState>.empty(detail: String? = null) {
    this.postValue(NetworkState(NetworkState.NetworkStateStatus.EMPTY, detail))
}

fun MutableLiveData<NetworkState>.error(detail: String) {
    this.postValue(NetworkState(NetworkState.NetworkStateStatus.ERROR, detail))
}

fun <T> MutableLiveData<ArrayList<T>>.setListValues(values: List<T>) {
    val value = arrayListOf<T>()
    value.addAll(values)
    this.postValue(value)
}

fun <T> MutableLiveData<ArrayList<T>>.addListValues(values: List<T>, clearBefore: Boolean = false) {
    val value = if (clearBefore) arrayListOf() else this.value ?: arrayListOf()
    value.addAll(values)
    this.postValue(value)
}

fun <T> MutableLiveData<ArrayList<T>>.empty() {
    this.postValue(arrayListOf())
}

fun <T> MutableLiveData<ArrayList<T>>.addFirstValue(v: T) {
    val value = this.value ?: arrayListOf()
    value.add(0, v)
    this.postValue(value)
}

fun <T> MutableLiveData<ArrayList<T>>.removeValue(v: T) {
    val value = this.value ?: arrayListOf()
    value.remove(v)
    this.postValue(value)
}

fun <T> MutableLiveData<ArrayList<T>>.isEmpty(): Boolean {
    return this.value?.isEmpty() ?: true
}

fun <T, U> MutableLiveData<Pair<T, U>>.setPair(first: T, second: U) {
    val value = Pair(first, second)
    this.postValue(value)
}

fun <T, U, V> MutableLiveData<Triple<T, U, V>>.setTriple(first: T, second: U, third: V) {
    val value = Triple(first, second, third)
    this.postValue(value)
}

fun <T> List<T>.second(): T {
    if (isEmpty() || size < 2)
        throw NoSuchElementException("List is empty or don't have 2 or more items")
    return this[1]
}

fun <T> List<T>.third(): T {
    if (isEmpty() || size < 3)
        throw NoSuchElementException("List is empty or don't have 3 or more items")
    return this[2]
}

fun <K, V> Map<out K, V>?.isNotNullOrEmpty(): Boolean {
    return this != null && !isEmpty()
}