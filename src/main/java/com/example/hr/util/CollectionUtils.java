package com.example.hr.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for collection operations
 */
public class CollectionUtils {

    /**
     * Check if collection is null or empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Check if collection is not null and not empty
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Check if map is null or empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Check if map is not null and not empty
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * Get first element from collection
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        return collection.iterator().next();
    }

    /**
     * Get last element from list
     */
    public static <T> T getLast(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    /**
     * Get element at index safely
     */
    public static <T> T getAt(List<T> list, int index) {
        if (isEmpty(list) || index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    /**
     * Get element at index with default value
     */
    public static <T> T getAt(List<T> list, int index, T defaultValue) {
        T value = getAt(list, index);
        return value != null ? value : defaultValue;
    }

    /**
     * Filter collection by predicate
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Map collection to another type
     */
    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * Find first element matching predicate
     */
    public static <T> T findFirst(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return null;
        }
        return collection.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * Check if any element matches predicate
     */
    public static <T> boolean anyMatch(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return false;
        }
        return collection.stream().anyMatch(predicate);
    }

    /**
     * Check if all elements match predicate
     */
    public static <T> boolean allMatch(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return false;
        }
        return collection.stream().allMatch(predicate);
    }

    /**
     * Check if no elements match predicate
     */
    public static <T> boolean noneMatch(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return true;
        }
        return collection.stream().noneMatch(predicate);
    }

    /**
     * Count elements matching predicate
     */
    public static <T> long count(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return 0;
        }
        return collection.stream().filter(predicate).count();
    }

    /**
     * Partition collection into two lists based on predicate
     */
    public static <T> Map<Boolean, List<T>> partition(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return Map.of(true, new ArrayList<>(), false, new ArrayList<>());
        }
        return collection.stream()
                .collect(Collectors.partitioningBy(predicate));
    }

    /**
     * Group collection by key extractor
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> keyExtractor) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.groupingBy(keyExtractor));
    }

    /**
     * Convert collection to map
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, 
                                            Function<T, K> keyMapper, 
                                            Function<T, V> valueMapper) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * Remove duplicates from collection
     */
    public static <T> List<T> removeDuplicates(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(new LinkedHashSet<>(collection));
    }

    /**
     * Reverse list
     */
    public static <T> List<T> reverse(List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        List<T> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);
        return reversed;
    }

    /**
     * Sort collection
     */
    public static <T extends Comparable<T>> List<T> sort(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Sort collection with comparator
     */
    public static <T> List<T> sort(Collection<T> collection, Comparator<T> comparator) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Get sublist safely
     */
    public static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        int size = list.size();
        int from = Math.max(0, Math.min(fromIndex, size));
        int to = Math.max(from, Math.min(toIndex, size));
        return new ArrayList<>(list.subList(from, to));
    }

    /**
     * Take first N elements
     */
    public static <T> List<T> take(Collection<T> collection, int n) {
        if (isEmpty(collection) || n <= 0) {
            return new ArrayList<>();
        }
        return collection.stream()
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Skip first N elements
     */
    public static <T> List<T> skip(Collection<T> collection, int n) {
        if (isEmpty(collection) || n <= 0) {
            return new ArrayList<>(collection);
        }
        return collection.stream()
                .skip(n)
                .collect(Collectors.toList());
    }

    /**
     * Paginate collection
     */
    public static <T> List<T> paginate(Collection<T> collection, int page, int pageSize) {
        if (isEmpty(collection) || page < 0 || pageSize <= 0) {
            return new ArrayList<>();
        }
        return collection.stream()
                .skip((long) page * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    /**
     * Flatten nested collections
     */
    public static <T> List<T> flatten(Collection<? extends Collection<T>> collections) {
        if (isEmpty(collections)) {
            return new ArrayList<>();
        }
        return collections.stream()
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Zip two collections
     */
    public static <A, B> List<Pair<A, B>> zip(Collection<A> first, Collection<B> second) {
        if (isEmpty(first) || isEmpty(second)) {
            return new ArrayList<>();
        }
        Iterator<A> iterA = first.iterator();
        Iterator<B> iterB = second.iterator();
        List<Pair<A, B>> result = new ArrayList<>();
        while (iterA.hasNext() && iterB.hasNext()) {
            result.add(new Pair<>(iterA.next(), iterB.next()));
        }
        return result;
    }

    /**
     * Chunk collection into smaller lists
     */
    public static <T> List<List<T>> chunk(Collection<T> collection, int chunkSize) {
        if (isEmpty(collection) || chunkSize <= 0) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>(collection);
        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += chunkSize) {
            chunks.add(list.subList(i, Math.min(i + chunkSize, list.size())));
        }
        return chunks;
    }

    /**
     * Intersect two collections
     */
    public static <T> List<T> intersect(Collection<T> first, Collection<T> second) {
        if (isEmpty(first) || isEmpty(second)) {
            return new ArrayList<>();
        }
        Set<T> set = new HashSet<>(second);
        return first.stream()
                .filter(set::contains)
                .collect(Collectors.toList());
    }

    /**
     * Union two collections
     */
    public static <T> List<T> union(Collection<T> first, Collection<T> second) {
        Set<T> set = new LinkedHashSet<>();
        if (isNotEmpty(first)) {
            set.addAll(first);
        }
        if (isNotEmpty(second)) {
            set.addAll(second);
        }
        return new ArrayList<>(set);
    }

    /**
     * Difference between two collections (elements in first but not in second)
     */
    public static <T> List<T> difference(Collection<T> first, Collection<T> second) {
        if (isEmpty(first)) {
            return new ArrayList<>();
        }
        if (isEmpty(second)) {
            return new ArrayList<>(first);
        }
        Set<T> set = new HashSet<>(second);
        return first.stream()
                .filter(item -> !set.contains(item))
                .collect(Collectors.toList());
    }

    /**
     * Symmetric difference (elements in either collection but not in both)
     */
    public static <T> List<T> symmetricDifference(Collection<T> first, Collection<T> second) {
        List<T> result = new ArrayList<>();
        result.addAll(difference(first, second));
        result.addAll(difference(second, first));
        return result;
    }

    /**
     * Check if collections are disjoint (no common elements)
     */
    public static <T> boolean areDisjoint(Collection<T> first, Collection<T> second) {
        if (isEmpty(first) || isEmpty(second)) {
            return true;
        }
        return Collections.disjoint(first, second);
    }

    /**
     * Shuffle collection
     */
    public static <T> List<T> shuffle(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>(collection);
        Collections.shuffle(list);
        return list;
    }

    /**
     * Get random element from collection
     */
    public static <T> T random(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        List<T> list = new ArrayList<>(collection);
        return list.get(new Random().nextInt(list.size()));
    }

    /**
     * Get N random elements from collection
     */
    public static <T> List<T> randomN(Collection<T> collection, int n) {
        if (isEmpty(collection) || n <= 0) {
            return new ArrayList<>();
        }
        List<T> shuffled = shuffle(collection);
        return take(shuffled, Math.min(n, shuffled.size()));
    }

    /**
     * Simple Pair class for zip operation
     */
    public static class Pair<A, B> {
        private final A first;
        private final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }
}
