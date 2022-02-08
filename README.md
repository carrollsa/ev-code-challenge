# ev code challenge
## Overview
This is my submission for a coding challenge asking to handle in-memory tracking of visit counts for about 20 million unique IP addresses per day. As an additional requirement, the code must be able to return the 100 most common IP addresses as fast as possible. Five questions about the challenge are answered below.

1) What would you do differently if you had more time?

- I would want to change the `Visitor` class to not declare `Visitor` objects equal if they aren’t totally equal, as well as to change the logic of the first condition of `update_top_visitors` to account for the change. The current `compareTo` method only compares their `ip` values and doesn’t consider visits, which allows an easy check for whether they are in `top_visitors` , but I don’t like the idea of an `equals()` method that does not check for total equality. Since the prompt suggested only spending so much time on this project, this is what I came up with, but I think we could do better.
- I would sort the return list for `top100()`as I’d think the most standard use cases would want a sorted list, but I didn’t include that step because the prompt does not ask for it, and the `PiorityQueue` (heap) is unsorted. No matter what we do to sort the list, the max number of elements in the dataset is 100, so it would still essentially be constant time.
- I would protect against corner case inputs. As it stands, this code assumes all input values are valid and not null. Additionally, it could be good to validate the IP String to fall into “0-255.0-255.0-255.0-255” bounds, which I have done before as a technical question.
- I would probably write it all in camelCase if I did it again because it is in Java, after all. I wanted the accessible methods to follow the naming convention given on the assignment, but it feels funny to me right now to write Java in snake case.

2) Runtime complexities:

- `request_handled()` : O(1)

	- `track_visit()` consists of 3 O(1) operations, at worst, barring a bad hash function for `visitors`.
	- `update_top_visitors()` deals with a `PriorityQueue` of max size 100, so the O(n) removal and O(log n) insertion times on `PriorityQueue` don’t affect our big O. The other conditions on `update_top_visitors()` are constant.

- `top100()`: O(1) 

	- Constant time, as this list will never be of a size greater than 100.

- `clear()` : O(1)

	- 2 O(1) operations.

3) How does your code work?

	My code consists of two classes, an `IpTracker` class containing all of the required methods and their requisite data structures, and a `Visitor` POJO that groups IPs with their visit count and allows for comparison and ordering. 

	My target for all operations was constant time. Although removal in a `PriorityQueue` is linear, the queue itself is of a fixed size, and a small one, so I think we’re well within the time constraints given. The functionality of each method is as follows:

	- `request_handled()` : Performs two steps. First, it tracks the visit by either creating a new `Visitor` object if the IP hasn’t been seen before, or, if it has, by incrementing the visit count on the existing `Visitor` object. Second, it updates the `top_visitors` queue, performing one of three actions. If the IP is already a “top visitor,” its count is incremented. Otherwise, if it is a new IP and `top_visitors` doesn’t yet have 100 IPs, it will add the `Visitor` object to the queue. If neither other condition is met, meaning it is an already-seen IP but is not currently in the `top_visitors` queue, it will check to see if it is now valid to be offered to the queue by comparing its number of visits to the exposed lowest-visit `Visitor` object in the queue.
	- `top100()` : Iterates over all `Visitor` objects in `top_ips` and adds their IP `String` to an `ArrayList`, returning the `ArrayList`.
	- `clear()` : Reassigns the member variables to new instances of their respective data structures.

4) What other approaches did you decide not to pursue?
	
	My initial thought is that we needed constant insert/update for the IP counts, so hashing came to mind, and any sort of `List` implementation was out. I thought initially of tracking the `Visitor` objects in a `HashSet`, before I remembered `get()` is not implemented on HashSets in Java. Before I settled on a min heap, I considered the idea of tracking everything IP counts in a `HashMap` and keeping a private minimum visit value represented in second `HashMap` representing the current “top 100,” but I couldn’t figure out a good way to keep updating the min without constantly iterating over the entire second map's keySet. The min heap solved that, as it keeps the lowest value accessible in constant time and reorganizes itself in log n time. 
	
	I didn’t have too many other ideas because I felt the min heap, at least for now, checked the major box of approaching constant time for the required methods, as it has O(1) lookup, O(1) removal, and O(log 100) → O(1) insertion/reorganization.

5) How would you test this?
	
	I would test this using unit tests in jUnit. I would make sure that each operation returns what it needs to under different conditions, including making sure out of bounds inputs are handled appropriately (once they’re actually handled). To make sure the speed is <300ms, I would use `System.nanoTime()` with a randomly generated 20 million+ unique IPs as the input.