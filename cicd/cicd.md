#CI/CD Improvements proposal

Hey <...>, I know we have talked about this before, but I'd like to share some of my collected thoughts
on the matter and try to articulate them to hopefully find a good compromise that can improve
 our iteration velocity without compromising our ability to deliver value to our customers.
 
 Actually, quite the contrary, I think improving our CI/CD capabilities would enable us to deliver
 more value to our customers by enabling our team to rapidly respond to both feature requests
 as well as customer-satisfaction tickets.  We would also probably be able tighten our SLA's with
 most of our clients.
 
 I'd like to address some of the points we've discussed before (please correct any bad assumptions I have made).
 
 * We have lots of customers, and each has their own AWS account (would be difficult to configure for each one)
 
 There is a ton of overlap between all of our clients.  Our teams all have an implicit
 (i.e. unwritten) understanding of how to onboard new clients...we just don't have an executable
 configuration that we can automate.  If we spent some resources to automate the commonalities,
 we would have more time (in the long run) to spend on delivering features and bug-fixes.
 Deployments would also become much more predictable.  AWS has a lot of tools for managing CI/CD
 across multiple accounts; we just need to spend a little time investigating our options.
 
 * code should be released at the end of every sprint.
 
 I don't think we need to change our practice of releasing code at the end of our sprints.  Our customers
 have come to expect this.  However, sufficiently mature CI/CD automation would allow us to
 easily (and optionally) increase the cadence of our releases with a high degree of confidence.
 This is a nice value-add option and it would allow us to easily pivot (e.g. in the case of 
 a major show-stopping bug, we could easily release and deploy a fix).
 
 * each team uses different technologies
 
 While this is true, there are still a lot of common deployment patterns between our teams.
 With a bit of coordination, I think our teams could come up with a nice set of requirements
 for the proposed automation.
 
 * we can't afford to take engineers off of product work to work on it
 
 I don't think we really know the scope of work required yet.  I think a relatively short meeting,
 to discuss what we would like, should give us a better idea.  I think we would also discover
 that the automation can be broken up into some pretty small work items that we can then order
 so that we get a lot of "bang for our buck" initially.
 
 I would really like to discuss this in more detail with our team leads...I think we have lot of
 opportunity to greatly improve our processes.
 
 
 -David 