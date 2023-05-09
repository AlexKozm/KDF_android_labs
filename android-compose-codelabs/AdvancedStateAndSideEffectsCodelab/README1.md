New functions:
StateFlow<>.collectAsStateWithLifecycle() - fun to collect values from this StateFlow and represents its latest value via State in a lifecycle-aware manner in Compose
rememberUpdatedState(value) - provides with the latest value of all recompositions
rememberCoroutineScope - scope bounded to this point in the composition
LifecycleEventObserver { _, event -> } - observer of lifecycle
produceState(init) {} - launches a coroutine scoped to the Composition that can push values into the returned State using the value property
derivedStateOf {} calculation block is executed every time the internal state changes, but the composable function only recomposes when the result of the calculation is different from the last one
