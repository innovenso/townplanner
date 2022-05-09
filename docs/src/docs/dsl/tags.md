# Tags

A *Tag* is a simple concept that can be used to categorize other concepts. It has a title, an optional color and 
an optional description.

```scala
val relevantForArchitecture: Tag = ea has Tag(title = "relevantForArchitecture")

val marketingTag: Tag = ea describes Tag(title = "marketingWorkstream", color=Color(255,100,100)) as { it =>
  it has Description("Indicates concepts considered part of the marketing workstream")
}
```

## Assigning tags to concepts

Once a tag is defined in the town plan, it can be assigned to other concepts, or at least those that support tags.
At the time of writing this documentation, the concepts that are taggable are *BusinessCapability* and *Technology*.

```scala
val townplanner: Tool =
  ea describes Tool(title = "Innovenso Townplanner") as { it =>
    ...
    it isTagged relevantForArchitecture
  }
```

## Usage in views

At the time of writing this, the business capability map one-pager shows the tags on business capabilities as colors.

The knowledge matrix view can be filtered using tags. 