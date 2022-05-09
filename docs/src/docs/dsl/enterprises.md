# Enterprise

The enterprise is the root of the enterprise town plan. Although it is not advised, it is possible
to describe more than 1 enterprise in one town plan, assigning business capabilities to one or the other.

```scala
  val wayneCorp: Enterprise =
    ea describes Enterprise(title = "Wayne Enterprises") as { it =>
      it has Description(
        "Wayne Enterprises, Inc., also known as WayneCorp is a fictional company appearing in American comic books published by DC Comics, commonly in association with the superhero Batman. Wayne Enterprises is a large, growing multinational company."
      )
    }
```

## Properties

### Description

An enterprise can have 0 or more descriptions.

```scala
  val wayneCorp: Enterprise =
    ea describes Enterprise(title = "Wayne Enterprises") as { it =>
      it has Description(
        "Wayne Enterprises, Inc., also known as WayneCorp is a fictional company appearing in American comic books published by DC Comics."
      )
      it has Description("It is commonly known in association with the superhero Batman.")
      it has Description("Wayne Enterprises is a large, growing multinational company.")
    }
```

### Links

```scala
val wayneCorp: Enterprise =
  ea describes Enterprise(title = "Wayne Enterprises") as { it =>
  ...
  it has Website("https://wayne.com")
  it has Wiki("https://en.wikipedia.org/wiki/Wayne_Enterprises")  
  ...
}
```

### SWOT

A business actor can have strengths, weaknesses, opportunities and threats.

```scala
val wayneCorp: Enterprise =
  ea describes Enterprise(title = "Wayne Enterprises") as { it =>
  ...
  it has Strength("It's big")
  it has Strength("It's rich")
  it has Weakness("CEO has connections with the League of Shadows")
  it has Threat(
    "Links with Batman, drawing attention from supervillains"
  )
  it has Opportunity(
    "it can grow it's market share in China"
  )
  ...
}
```

## Relationships

### Serving

An enterprise can be the target of a *Serving* relationship, mostly used for business capabilities, business actors,
projects, decisions and principles.

```scala
val offeringSuperheroServices: BusinessCapability =
    ea describes BusinessCapability(title = "Offering Superhero Services") as { it =>
        it serves enterprises.wayneCorp
    }

val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  he serves enterprises.wayneCorp
}
```