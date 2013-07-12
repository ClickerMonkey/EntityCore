#include <EntityType.h>
#include <EntityTypeCustom.h>
#include <EntityCore.h>

using namespace std;

EntityType::EntityType(const size_t id, const EntityType *parent, const IdMap &components, const IdMap &controllers, const size_t viewId, const AnyMemory &defaultComponents, const vector<size_t> &offsets)
  : id(id), parent(parent), components(components), controllers(controllers), viewId(viewId), defaultComponents(defaultComponents), offsets(offsets)
{
}

EntityType::~EntityType() 
{
}

EntityType* EntityType::extend(const size_t entityTypeId) 
{
  return new EntityType( entityTypeId, this, components, controllers, viewId, defaultComponents, offsets );
}

void EntityType::setComponentAlias(const size_t componentId, const size_t aliasId) 
{
  components.alias(componentId, aliasId);
}

void EntityType::setControllerAlias(const size_t controllerId, const size_t aliasId) 
{
  controllers.alias(controllerId, aliasId);
}

void EntityType::setView(const size_t view) 
{
  viewId = view;
}

bool EntityType::add(const size_t componentId)
{
  bool missing = !hasComponent(componentId);

  if (missing)
  {
    components.add(componentId);
    offsets.push_back(defaultComponents.append(EntityCore::getComponent(componentId)->defaultValue));
  }

  return missing;
}

bool EntityType::addController(const size_t controllerId) 
{
  bool missing = !hasController(controllerId);

  if (missing)
  {
    controllers.add(controllerId);  
  }

  return missing;
}

void EntityType::setDefaultComponents(AnyMemory& components) 
{
  components.append(defaultComponents);
}

EntityType* EntityType::addCustomComponent(const size_t componentId)
{
  EntityType *custom = new EntityTypeCustom( CUSTOM, this, components, controllers, viewId, defaultComponents, offsets);
  custom->add(componentId);
  return custom;
}

EntityType* EntityType::addCustomController(const size_t controllerId)
{
  EntityType *custom = new EntityTypeCustom( CUSTOM, this, components, controllers, viewId, defaultComponents, offsets);
  custom->addController(controllerId);
  return custom;
}

EntityType* EntityType::setCustomView(const size_t viewId)
{
  EntityType *custom = new EntityTypeCustom( CUSTOM, this, components, controllers, viewId, defaultComponents, offsets);
  custom->setView(viewId);
  return custom;
}

bool EntityType::isCustom() 
{
  return false;
}