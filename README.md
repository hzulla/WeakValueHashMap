WeakValueHashMap
================

A java implementation of a weak value HashMap using generics.

The desired behaviour of an in-memory cache is to keep a weak reference to the
cached object, this will allow the garbage collector to remove an object from
memory once it isn't needed anymore.

A HashMap doesn't help here since it will keep hard references for key and value
objects. A WeakHashMap doesn't either, because it keeps weak references to the
key objects, but we want to track the value objects.

This implementation of a Map uses a WeakReference to the value objects. Once the
garbage collector decides it wants to finalize a value object, it will be
removed from the map automatically. 