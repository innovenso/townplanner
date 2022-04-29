# Business Actors

Business Actor is a concept in the Archimate language, where is it described as *A business actor represents a business entity that is capable of performing behavior.*.

The townplanner DSL has 4 different types of business actors, representing respectively a noun, like *customer* or *employee*, an individual like *Bruce Wayne*, or a team or organization:

```scala
val villain: Actor = ea has Actor(title = "Supervillain")
val bruceWayne: Person = ea has Person(title = "Bruce Wayne")
val justiceLeague: Team = ea has Team(title = "Justice League")
val leagueOfShadows: Organisation = ea has Organisation(title = "League of Shadows")
```

## Properties

### Description

A business actor can have 0 or more descriptions.

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  he has Description("I'm Batman")
  he has Description("Lost his parents at a young age and has become the masked vigilante because of that.")
  ...
}
```

### Links

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he has Website("https://linkedin.com/brucewayne")
  he has Wiki("https://dc.fandom.com/wiki/Batman_(Bruce_Wayne)")  
  ...
}
```

### External ID

An external ID is a reference to the description, documentation, implementation or anything else in some external system, for example in another EA tool, or in a cloud platform.

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he isIdentifiedAs "{42a5e9d4-188d-4509-8145-eaac65ab86b8}" on "Sparx EA"
  ...
}
```

### SWOT

A business actor can have strengths, weaknesses, opportunities and threats.

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he has Strength("he has lots of gadgets")
  he has Strength("Alfred helps him a lot")
  he has Weakness("He is a bit traumatized and unstable")
  he has Threat(
    "He seems to have a conflict with Superman, which is not the best idea in the world"
  )
  he has Opportunity(
    "if he works together with the Justice League, he will be much stronger"
  )
  ...
}
```

## Relationships

### Flow

A business actor can be both the source and the target of a flow relationship. A flow relationship can be defined on the elements on either side.

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he uses systems.bcms
  ...
}
```

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he uses(systems.bcms, "uses to control batcave")
  ...
}
```

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he isUsing systems.bcms and { that =>
    that has Description("He likes to stare at the screens for a long time, trying to figure out what evil plans The Joker is up to.")
  }
  ...
}
```

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he isFlowingTo(systems.bcms, "uses to control batcave") and { that =>
    that has Description("He likes to stare at the screens for a long time, trying to figure out what evil plans The Joker is up to.")
  }
  ...
}
```

```scala
val bcms: ItSystem = ea describes ItSystem(title = "BatCave Management System") as { it =>
    it isUsedBy actors.bruceWayne
}
```

```scala
val bcms: ItSystem = ea describes ItSystem(title = "BatCave Management System") as { it =>
    it isBeingUsedBy actors.bruceWayne and { that =>
      that has Description("Bruce likes to stare at this intensely")
    }
}
```

### Deliver

The delivery relationship indicates that a system, service, project, ... is delivered by a team, an organization or even an individual. A business actor can be the source of a delivery relationship.

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he delivers systems.bcms
  ...
}
```

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he isDelivering systems.bcms and { that =>
    that has Description("Admittedly, he needed Lucius Fox")
  }
  ...
}
```

```scala
val bcms: ItSystem = ea describes ItSystem(title = "BatCave Management System") as { it =>
    it isBeingDeliveredBy actors.bruceWayne and { that =>
      that has Description("Admittedly, he needed Lucius Fox")
    }
}
```

```scala
val bcms: ItSystem = ea describes ItSystem(title = "BatCave Management System") as { it =>
    it isDeliveredBy actors.bruceWayne
}
```


### Influence

A business actor can influence decisions and projects, so a business actor can be the source of an influencing relationship.

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he influences projects.cmsMigration
  he influences(projects.pspImplementation, "he does not want any links with the underworld")
  he influences decisions.cmsVendorSelection  
  ...
}
```

```scala
val mayhem: ItProject = ea describes ItProject(title = "Project Mayhem") as { it =>
  ...
  it isInfluencedBy actors.bruceWayne
  ...
}
```

### Serve

A business actor can be the source of a serving relationship, typically used to assign the actor to an enterprise.

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he serves enterprises.wayneCorp  
  ...
}
```

### Stakeholder

A business actor can be a stakeholder for projects and decisions.

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he hasInterestIn projects.cmsMigration
  he isStakeholderFor(projects.pspImplementation, "he does not want any links with the underworld")
  ...
}
```

### RACI

The RACI model is a matrix that is for documenting the roles and responsibilities of people in a project or decision. The 4 letters stand for Responsible, Accountable, Consulted and Informed.

A business actor can be the source of any of these 4.

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he isResponsibleFor projects.cmsMigration
  he isAccountableFor projects.pspImplementation
  he isConsultedAbout decisions.cdpVendorSelection
  decisions.allDecisions.forEach(decision => he isInformedAbout decision)  
  ...
}
```

Note the last line, showing that the townplan is just Scala code, so you can use any Scala language construct to define the town plan, here demonstrated by a simple iteration over all decisions. 

### Composition

A business actor can be the source as well as the target of a composition relationship. This can be used to model the composition of delivery teams for example.

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he isPartOf justiceLeague
  ...
}
```

### Knowledge/Expertise

The knowledge relationship can indicate the level of knowledge an individual, team or organisation has about a platform, system, component or technology. This information can then be used to generate a knowledge matrix.

```scala
val bruceWayne: Person = ea describes Person(title = "Bruce Wayne") as { he =>
  ...
  he hasKnowledgeOf(level = Expert) technologies.java
  ...
}
```

The levels of knowledge are *NoKnowledge*, *Learner*, *Knowledgeable*, *HighlyKnowledgeable*, *Expert*. The default level when the parameter is omitted is *Knowledgeable*.