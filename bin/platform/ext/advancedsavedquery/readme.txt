Short introduction on AdvancedSavedQuery

Version: 0.1
Date: 19/02/2008

Purpose:

Fixes the short falls of the SavedQuery concept. E.g.
- empty parameters
- JOINs (e.g. finding an order by a customers address)
- in between dates search


How does it works?

This module is an extension of the SavedQuery approach. However rather writing the whole query in stone the where part 
of it stays flexible and will be generated on the fly. The user can specify how the system handles empty fields for instance.
The search is available to the actual user (e.g. pm) at the advanced search option in the organiser search area.

After the user has filled the parameter he/she likes the system generates the actual flexible search on the fly and execute
it afterwards.

Setting up an AdvancedSavedQuery

1. Open up the hmc
2. Browse to System -> Saved Queries
3. Click on new and select AdvancedSavedQuery
4. Provide an identifier
5. Optional  provide a more useful name and a description. Both are later visible to the user
6. Put in the Query. You'd need Flexible Search Knowledge to do so. A sample Query could be
Select {c.pk} FROM {Customer as c LEFT JOIN Address as a ON {c.pk} ={a.owner}  } Where <param>
The <param> part is a replacement pattern, that will be replaced before the Query will be executed.
7. Choose the result type. In this case Customer
8. Save
9. Now create the WherePart for the pattern. (If you don't provide a pattern, the system will remove
the complete WHERE part (incl. WHERE)
10. as the replacePattern put in your pattern. In this case <param>
11. specify if the parameter should be connected using AND or OR.
12. Create a list of dynamic parameter the user should have available to fill in.
13. You got three options:
13.1 Simple - if you used something like WHERE {c.name}=?name in your query, this is the type you should use
13.2 Typed - if you JOIN two or more types then for the parameters that do not belong to your result type this should be used.
13.3 ComposeType (ResultType) - the internal name is not quite right. The result type of the query actually specifies which parameter
names are actually available. ResultType and Typed behave similiar with the exception that in the case of ResultType typedSearchParemeter 
and enclosingType are pre-populated.
14. In the next editor, choose the comparator first. Unfortunately in the drop down box can't present you with symbols
14.1 equals - =
14.2 contains - LIKE %thevalue%
14.3 gt - >
14.4 gtequals - >=
14.5 lt - <
14.6 ltequals - <=
14.7 startwidth - LIKE thevalue%
15. define how the system should deal with empty values for a parameter
15.1 Ignore - if empty remove parameter from query
15.2 As is - leave the parameter in no matter what value it has
15.3 Trim & Ignore - trims the value first, if then empty removes the parameter from the list too.
16. provide a join Alias if your query is a JOIN
17. provide a local, more human readable name
This step is available for Typed and ResultType parameter only. 
18. Select the search parameter you want to search on. In the case of the Typed parameter you have to choose the enclosingType first.
19. Save all editors.
20. You can review your query in the generatedFlexibleSearch field. 

Using an Advanced Query.
1. Select the element you want to search on (in this case customer)
2. Select the advanced search drop down list on the right hand site in the search area.
3. Fill in your fields and submit.

