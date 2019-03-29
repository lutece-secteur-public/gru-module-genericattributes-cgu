![](https://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=module-genericattributes-cgu-deploy)
# Module genericattributes-cgu

## Introduction

Ce module fournit un *generic attribute* pour les Conditions G&eacute;n&eacute;rales d'Utilisations (CGU). Pour cela, le module utilise le **plugin-cgu** . L'utilisateur peut voir la CGU. Il doit accepter la CGU. S'il existe un&acirc;ge minimum d'acceptation, alors l'utilisateur doit&eacute;galement certifier qu'il est plus ag&eacute; que cet&acirc;ge minimum.

## Instanciation

Comme tous les *generic attributes* , le plugin Lutece qui utilise ce *generic attribute* doit fournir les templates&agrave; utiliser pour la cr&eacute;ation, la modification et la visualisation du *generic attribute* en back-office et en front-office.

La classe abstraite `fr.paris.lutece.plugins.genericattributes.modules.cgu.service.entrytype.AbstractEntryTypeCgu` permet de g&eacute;rer le *generic attribute* . Pour fournir les templates, il faut cr&eacute;er un classe h&eacute;ritant de cette classe abstraite et impl&eacute;menter les m&eacute;thodes suivantes :
 
*  `getTemplateHtmlForm( Entry entry, boolean bDisplayFront )` : donne le template servant&agrave; renseigner la question. Le bool&eacute;en `bDisplayFront` permet de savoir s'il faut donner le template pour le front-office ou pour le back-office.
*  `getTemplateCreate( Entry entry, boolean bDisplayFront )` : donne le template servant&agrave; cr&eacute;er la configuration du *generic attribute* . Le bool&eacute;en `bDisplayFront` permet de savoir s'il faut donner le template pour le front-office ou pour le back-office.
*  `getTemplateModify( Entry entry, boolean bDisplayFront )` : donne le template servant&agrave; modifier la configuration du *generic attribute* . Le bool&eacute;en `bDisplayFront` permet de savoir s'il faut donner le template pour le front-office ou pour le back-office.
*  `getTemplateEntryReadOnly( boolean bDisplayFront )` : donne le template servant&agrave; afficher les informations renseign&eacute;es dans la question. Le bool&eacute;en `bDisplayFront` permet de savoir s'il faut donner le template pour le front-office ou pour le back-office.


Une impl&eacute;mentation par d&eacute;faut est donn&eacute;e dans ce module. Elle correspond&agrave; la classe `fr.paris.lutece.plugins.genericattributes.modules.cgu.service.entrytype.EntryTypeCgu` . **Attention :** Cette impl&eacute;mentation est faite pour&ecirc;tre utilis&eacute;e avec le **plugin-form** . Les templates fournis ne fonctionneront pas avec un autre plugin Lutece. Si vous utilisez ce module avec un autre plugin Lutece, il faut alors soit surcharger les templates dans le site, soit fournir votre propre impl&eacute;mentation.

Il faut ensuite que l'impl&eacute;mentation soit pr&eacute;sente dans le contexte Spring pour que Lutece la d&eacute;tecte. L'impl&eacute;mentation par d&eacute;faut de ce module est pr&eacute;sente dans le fichier `genericattributes_cgu_context.xml` .

Il faut enfin r&eacute;f&eacute;rencer l' `EntryType` dans la table `genatt_entry_type` . L'injection de l'impl&eacute;mentation par d&eacute;faut dans la table est pr&eacute;sente dans les fichiers SQL du module.

Une fois l'instanciation du *generic attribute* correctement r&eacute;f&eacute;renc&eacute;e, il est disponible dans votre application, avec les autres *generic attributes* .

## Configuration

Il faut que la CGU&agrave; utiliser soit d'abord configur&eacute;e dans le *plugin-cgu* .

 **Attention :** Si une nouvelle version de la CGU est publi&eacute;e dans le *plugin-cgu* , **il faut** retourner dans la configuration du *generic attribute* CGU en back-office et **r&eacute;enregistrer** (m&ecirc;me s'il n'y a aucune modification) pour que les nouvelles donn&eacute;es de la CGU soient prises en compte dans le *generic attribute* .

 **La configuration pr&eacute;sent&eacute;e ci-dessous ne s'applique que pour l'impl&eacute;mentation par d&eacute;faut fournie dans le module, c'est-&agrave;-dire pour le plugin-form.** 

En back-office, au niveau des questions du formulaire, un type de question **Conditions g&eacute;n&eacute;rales d'utilisations** appara&icirc;t. Sa configuration est la suivante :
 
*  `Titre` : le titre de la question.
*  `Code` : le code de la question.
*  `Code CGU` : le code de la CGU&agrave; utiliser. Ce code correspond&agrave; celui d&eacute;fini dans le *plugin-cgu* pour la CGU.
*  `Aide` : aide affich&eacute;e sous la question.
*  `Classes CSS` : classes CSS utilis&eacute;es pour la question.


## Usage

 **L'utilisation pr&eacute;sent&eacute;e ci-dessous ne s'applique que pour l'impl&eacute;mentation par d&eacute;faut fournie dans le module, c'est-&agrave;-dire pour le plugin-form.** 

Lorsqu'une question de type CGU est configur&eacute;e dans le formulaire, une case&agrave; cocher appara&icirc;t dans la page front-office du formulaire. L'utilisateur est oblig&eacute; de cocher cette case&agrave; cocher pour valider le formulaire. Sinon, un message d'erreur appara&icirc;t. L'utilisateur peut voir le texte de la CGU en cliquant sur le lien pr&eacute;sent dans la question. Un nouvel onglet avec le texte s'ouvre alors.

Une deuxi&egrave;me case&agrave; cocher peut&ecirc;tre affich&eacute;e si la CGU n&eacute;cessite un&acirc;ge minimum pour&ecirc;tre accept&eacute;e. L'utilisateur est alors&eacute;galement oblig&eacute; de cocher cette case&agrave; cocher pour valider le formulaire, afin de certifier son&acirc;ge. Sinon, un message d'erreur appara&icirc;t.

Voici un exemple d'affichage avec&acirc;ge minimum&agrave; 15 ans, et message d'aide :![](https://dev.lutece.paris.fr/plugins/module-genericattributes-cgu/images/questionCGU.png)


[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/module-genericattributes-cgu/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*