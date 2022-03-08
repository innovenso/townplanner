# Core Concepts

The domain model of the Innovenso Townplanner is structured around a few concepts:

* Elements
* Relationships
* Properties
* Views


## Elements

Elements are basically the *boxes* in diagrams. There are many specializations of the Element, such as *IT System*, *Business Actor*, *Business Capability*, ...

Elements typically have a unique key to identify it, a title and a description. Specializations can have more properties.

## Relationships

Relationships are the *lines* in diagrams. There are quite a few types of relationships, including *flow*, *realization*, *impact*, ...

Like elements, relationships have a unique key, a title and a description.

## Properties

Properties provide the capability to enrich elements and relationships in the model, with extra information. There are properties to add
*documentation*, *architecture verdict*, *SWOT analysis*, ...

## Views

Views are used to render diagrams and documents about a part of the town plan. Like Archimate Viewpoints, a view is a selection of a relevant subset of the elements of the townplan and their relationships.