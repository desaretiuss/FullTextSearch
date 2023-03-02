### Description
This is a command-line application, written in Java, which implements full-text search using PostgreSQL.
Program accepts two types of commands:
1. The **index** command, which adds a document to the index:
   `index doc-id token1 … tokenN`
2. The **query** command, which queries the database for an expression:
   `query expression`

### Libraries used in project
+ SpringBoot
+ SpringBoot JDBC
+ PostgreSQL
+ Apache Commons
+ Lombok

#### Basics of searching in postgresql
Full-text Searching (FTS) is the ability to find text documents that satisfy a **query** (e.g. a simple set of words)
In postgresql, a document should first be **preprocessed**.

#### Preprocessing

+ Parsing documents into tokens: raw text is split into smaller, meaningful and categorized parts, called **tokens**.
+ Converting tokens into lexemes. A **lexeme** is a token brought into its base form, with prefixes, suffixes etc. removed from it.
+ Storing preprocessed documents in a data structure optimized for searching, `ts_vector`.

### Two data types: tsvector and tsquery

+ `tsvector` is a particular datatype to store the preprocessed document in postgresql.
Searching and ranking are performed **entirely** on the tsvector representation of a document.
<br>

+ tsvector type removes duplicates and sorts the lexemes. See example:

```postgresql
SELECT 'a fat cat sat on a mat and ate a fat rat'::tsvector;
tsvector
----------------------------------------------------
'a' 'and' 'ate' 'cat' 'fat' 'mat' 'on' 'rat' 'sat'
```

**N.B.:** tsvector itself does not perform any word normalization; it assumes the words it is given are already normalized.
As you can see the following result, it can be considered non-normalized, but tsvector doesn't care.
This leads to unexpected results when searching, so be sure to normalize the input via `to_tsvector`.

+ `tsquery`, which stores terms (lexemes) that are to be searched for.
Search terms can be combined using the Boolean operators &, |, ! (AND, OR, NOT).
Another important operator is the *Phrase search* operator:  <-> (FOLLOWED BY).
Parentheses can be used to enforce grouping of these operators.

### Basic Text Matching

Full text searching in PostgreSQL is based on the match operator `@@`
Match operator `@@` needs two arguments, one of type `tsvector` and the other of type `tsquery`.

```postgresql
tsvector @@ tsquery → boolean
-- Does tsvector match tsquery? 
```
+ Usage example:
```postgresql
to_tsvector('fat cats ate rats') @@ to_tsquery('cat & rat') → t
```

### Sources:
[1] [Postgresql Documentation - Fulltext Search](https://www.postgresql.org/docs/current/textsearch-intro.html#TEXTSEARCH-DOCUMENT)

### Other possible alternatives:
[Apache Solr](https://solr.apache.org/), [Apache Lucene](https://lucene.apache.org/) and [Elasticsearch](https://www.elastic.co/) are some
very powerful libraries used especially in Java world. They support distributed solutions, real-time indexing etc. Alongside them, there are
also much more lightweight and easy-to use libraries written in different languages.

